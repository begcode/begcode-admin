package com.begcode.monolith.taskjob.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.JobStatus;
import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.repository.TaskJobConfigRepository;
import com.begcode.monolith.taskjob.service.dto.TaskJobConfigDTO;
import com.begcode.monolith.taskjob.service.mapper.TaskJobConfigMapper;
import com.begcode.monolith.web.rest.errors.CommonException;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.taskjob.domain.TaskJobConfig}.
 */
@SuppressWarnings("UnusedReturnValue")
public class TaskJobConfigBaseService<R extends TaskJobConfigRepository, E extends TaskJobConfig>
    extends BaseServiceImpl<TaskJobConfigRepository, TaskJobConfig> {

    private final Logger log = LoggerFactory.getLogger(TaskJobConfigBaseService.class);
    private final List<String> relationNames = List.of();

    protected final Scheduler scheduler;

    protected final TaskJobConfigRepository taskJobConfigRepository;

    protected final CacheManager cacheManager;

    protected final TaskJobConfigMapper taskJobConfigMapper;

    public TaskJobConfigBaseService(
        Scheduler scheduler,
        TaskJobConfigRepository taskJobConfigRepository,
        CacheManager cacheManager,
        TaskJobConfigMapper taskJobConfigMapper
    ) {
        this.scheduler = scheduler;
        this.taskJobConfigRepository = taskJobConfigRepository;
        this.cacheManager = cacheManager;
        this.taskJobConfigMapper = taskJobConfigMapper;
    }

    /**
     * Save a taskJobConfig.
     *
     * @param taskJobConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public TaskJobConfigDTO save(TaskJobConfigDTO taskJobConfigDTO) {
        log.debug("Request to save TaskJobConfig : {}", taskJobConfigDTO);
        TaskJobConfig taskJobConfig = taskJobConfigMapper.toEntity(taskJobConfigDTO);

        this.saveOrUpdate(taskJobConfig);
        return findOne(taskJobConfig.getId()).orElseThrow();
    }

    /**
     * Update a taskJobConfig.
     *
     * @param taskJobConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskJobConfigDTO update(TaskJobConfigDTO taskJobConfigDTO) {
        log.debug("Request to update TaskJobConfig : {}", taskJobConfigDTO);
        TaskJobConfig taskJobConfig = taskJobConfigMapper.toEntity(taskJobConfigDTO);

        this.saveOrUpdate(taskJobConfig);
        return findOne(taskJobConfig.getId()).orElseThrow();
    }

    /**
     * Partially update a taskJobConfig.
     *
     * @param taskJobConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<TaskJobConfigDTO> partialUpdate(TaskJobConfigDTO taskJobConfigDTO) {
        log.debug("Request to partially update TaskJobConfig : {}", taskJobConfigDTO);

        return taskJobConfigRepository
            .findById(taskJobConfigDTO.getId())
            .map(existingTaskJobConfig -> {
                taskJobConfigMapper.partialUpdate(existingTaskJobConfig, taskJobConfigDTO);

                return existingTaskJobConfig;
            })
            .map(tempTaskJobConfig -> {
                taskJobConfigRepository.save(tempTaskJobConfig);
                return taskJobConfigMapper.toDto(taskJobConfigRepository.selectById(tempTaskJobConfig.getId()));
            });
    }

    /**
     * Get all the taskJobConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<TaskJobConfigDTO> findAll(Page<TaskJobConfig> pageable) {
        log.debug("Request to get all TaskJobConfigs");
        return this.page(pageable).convert(taskJobConfigMapper::toDto);
    }

    /**
     * Get one taskJobConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TaskJobConfigDTO> findOne(Long id) {
        log.debug("Request to get TaskJobConfig : {}", id);
        return Optional.ofNullable(taskJobConfigRepository.selectById(id))
            .map(taskJobConfig -> {
                Binder.bindRelations(taskJobConfig);
                return taskJobConfig;
            })
            .map(taskJobConfigMapper::toDto);
    }

    /**
     * Delete the taskJobConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete TaskJobConfig : {}", id);

        taskJobConfigRepository.deleteById(id);
    }

    private static final String taskIdPrefix = "huayu_task_";

    /**
     * 保存&启动定时任务
     */
    @Transactional
    public boolean saveAndScheduleJob(TaskJobConfig taskJobConfig) {
        // DB设置修改
        boolean success = this.save(taskJobConfig);
        if (success) {
            if (JobStatus.NORMAL.equals(taskJobConfig.getJobStatus())) {
                // 定时器添加
                this.schedulerAdd(
                        taskIdPrefix + taskJobConfig.getId(),
                        taskJobConfig.getJobClassName().trim(),
                        taskJobConfig.getCronExpression().trim(),
                        taskJobConfig.getParameter()
                    );
            }
        }
        return success;
    }

    /**
     * 恢复定时任务
     */
    @Transactional
    public boolean resumeJob(Long id) {
        TaskJobConfig taskJobConfig = taskJobConfigRepository.selectById(id);
        schedulerDelete(taskIdPrefix + taskJobConfig.getId());
        schedulerAdd(
            taskIdPrefix + taskJobConfig.getId(),
            taskJobConfig.getJobClassName().trim(),
            taskJobConfig.getCronExpression().trim(),
            taskJobConfig.getParameter()
        );
        taskJobConfig.setJobStatus(JobStatus.NORMAL);
        return this.updateById(taskJobConfig);
    }

    /**
     * 编辑&启停定时任务
     * @throws SchedulerException
     */
    @Transactional
    public boolean editAndScheduleJob(TaskJobConfig taskJob) throws SchedulerException {
        if (JobStatus.NORMAL.equals(taskJob.getJobStatus())) {
            schedulerDelete(taskIdPrefix + taskJob.getId());
            schedulerAdd(
                taskIdPrefix + taskJob.getId(),
                taskJob.getJobClassName().trim(),
                taskJob.getCronExpression().trim(),
                taskJob.getParameter()
            );
        } else {
            scheduler.pauseJob(JobKey.jobKey(taskIdPrefix + taskJob.getId()));
        }
        return this.updateById(taskJob);
    }

    /**
     * 删除&停止删除定时任务
     */
    @Transactional
    public boolean deleteAndStopJob(TaskJobConfig job) {
        schedulerDelete(taskIdPrefix + job.getId());
        return this.removeById(job.getId());
    }

    public void execute(Long id) throws Exception {
        TaskJobConfig taskJobConfig = taskJobConfigRepository.selectById(id);
        if (Objects.isNull(taskJobConfig)) {
            throw new CommonException("notExist", "定时任务不存");
        }
        String jobName = taskJobConfig.getJobClassName().trim();
        Date startDate = new Date();
        DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.systemDefault());
        String ymd = yyyyMMddHHmmss.format(startDate.toInstant());
        String identity = jobName + ymd;
        startDate.setTime(startDate.getTime() + 100L);
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
            .withIdentity(identity, "immediate_group")
            .startAt(startDate)
            .build();
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobName).getClass())
            .withIdentity(identity)
            .usingJobData("parameter", taskJobConfig.getParameter())
            .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    @Transactional
    public void pause(Long id) {
        TaskJobConfig quartzJob = taskJobConfigRepository.selectById(id);
        schedulerDelete(taskIdPrefix + quartzJob.getId());
        quartzJob.setJobStatus(JobStatus.PAUSED);
        this.updateById(quartzJob);
    }

    /**
     * 添加定时任务
     *
     * @param jobClassName 类名
     * @param cronExpression 定时表达式
     * @param parameter 参数
     */
    private void schedulerAdd(String id, String jobClassName, String cronExpression, String parameter) {
        try {
            // 启动调度器
            scheduler.start();

            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                .withIdentity(id)
                .usingJobData("parameter", parameter)
                .build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(id).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new CommonException("createJobFailure", "创建定时任务失败");
        } catch (RuntimeException e) {
            throw new CommonException("runJobFailure", "创建定时任务失败");
        } catch (Exception e) {
            throw new CommonException("classNotFound", "后台找不到该类名：" + jobClassName);
        }
    }

    /**
     * 删除定时任务
     *
     * @param id 任务ID
     */
    private void schedulerDelete(String id) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(id));
            scheduler.unscheduleJob(TriggerKey.triggerKey(id));
            scheduler.deleteJob(JobKey.jobKey(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("deleteJobFailure", "删除定时任务失败");
        }
    }

    private static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }

    /**
     * Update specified field by taskJobConfig
     */
    @Transactional
    public void updateBatch(TaskJobConfigDTO changeTaskJobConfigDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<TaskJobConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeTaskJobConfigDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<TaskJobConfig> taskJobConfigList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(taskJobConfigList)) {
                taskJobConfigList.forEach(taskJobConfig -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                taskJobConfig,
                                relationName,
                                BeanUtil.getFieldValue(taskJobConfigMapper.toEntity(changeTaskJobConfigDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(taskJobConfig, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            TaskJobConfig byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
