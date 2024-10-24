package com.begcode.monolith.taskjob.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.repository.TaskJobConfigRepository;
import com.begcode.monolith.taskjob.service.criteria.TaskJobConfigCriteria;
import com.begcode.monolith.taskjob.service.dto.TaskJobConfigDTO;
import com.begcode.monolith.taskjob.service.mapper.TaskJobConfigMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link TaskJobConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link TaskJobConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link TaskJobConfigDTO}列表{@link List} 或 {@link TaskJobConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class TaskJobConfigQueryService implements QueryService<TaskJobConfig> {

    private static final Logger log = LoggerFactory.getLogger(TaskJobConfigQueryService.class);

    protected final TaskJobConfigRepository taskJobConfigRepository;

    protected final TaskJobConfigMapper taskJobConfigMapper;

    public TaskJobConfigQueryService(TaskJobConfigRepository taskJobConfigRepository, TaskJobConfigMapper taskJobConfigMapper) {
        this.taskJobConfigRepository = taskJobConfigRepository;
        this.taskJobConfigMapper = taskJobConfigMapper;
    }

    /**
     * Return a {@link List} of {@link TaskJobConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<TaskJobConfigDTO> findByCriteria(TaskJobConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<TaskJobConfig> queryWrapper = createQueryWrapper(criteria);
        return taskJobConfigMapper.toDto(Binder.joinQueryList(queryWrapper, TaskJobConfig.class));
    }

    /**
     * Return a {@link IPage} of {@link TaskJobConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<TaskJobConfigDTO> findByCriteria(TaskJobConfigCriteria criteria, Page<TaskJobConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<TaskJobConfig> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, TaskJobConfig.class, page).convert(taskJobConfig -> {
            Binder.bindRelations(taskJobConfig, new String[] {});
            return taskJobConfigMapper.toDto(taskJobConfig);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(TaskJobConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return taskJobConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, TaskJobConfigCriteria criteria) {
        return (List<T>) taskJobConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, TaskJobConfigCriteria criteria) {
        return taskJobConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link TaskJobConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<TaskJobConfig> createQueryWrapper(TaskJobConfigCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            TaskJobConfigCriteria keywordsCriteria = new TaskJobConfigCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.jobClassName().setContains(keywords);
            keywordsCriteria.cronExpression().setContains(keywords);
            keywordsCriteria.parameter().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            TaskJobConfigCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<TaskJobConfig> queryWrapper = new DynamicJoinQueryWrapper<>(TaskJobConfigCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, TaskJobConfig.class);
    }

    /**
     * Function to convert {@link TaskJobConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<TaskJobConfig> createQueryWrapperNoJoin(TaskJobConfigCriteria criteria) {
        QueryWrapper<TaskJobConfig> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, TaskJobConfig.class);
    }

    /**
     * Return a {@link IPage} of {@link TaskJobConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<TaskJobConfigDTO> findByQueryWrapper(QueryWrapper<TaskJobConfig> queryWrapper, Page<TaskJobConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return taskJobConfigRepository.selectPage(page, queryWrapper).convert(taskJobConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(TaskJobConfigCriteria criteria) {
        QueryWrapper<TaskJobConfig> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.job_class_name", criteria.getJobClassName());
        fieldNameMap.put("self.cron_expression", criteria.getCronExpression());
        fieldNameMap.put("self.parameter", criteria.getParameter());
        fieldNameMap.put("self.description", criteria.getDescription());
        fieldNameMap.put("self.job_status", criteria.getJobStatus());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, TaskJobConfig.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
