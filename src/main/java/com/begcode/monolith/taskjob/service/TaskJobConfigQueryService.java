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
import com.diboot.core.binding.query.BindQuery;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.google.common.base.CaseFormat;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.CriteriaUtil;
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
        return taskJobConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
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
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<TaskJobConfig> queryWrapper = new DynamicJoinQueryWrapper<>(TaskJobConfigCriteria.class, fields);
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
        Map<String, Map<String, Object>> fieldNameMap = CriteriaUtil.getNonIgnoredAndNonNullFields(criteria);
        fieldNameMap.forEach((key, value) -> {
            // 获得value 对象BindQuery 注解的column属性值
            Filter<?> filter = (Filter<?>) value.get("value");
            BindQuery bindQuery = (BindQuery) value.get("bindQuery");
            String column = bindQuery.column();
            if (column.startsWith("self.")) {
                getAggregateAndGroupBy(filter, column, "", selectFields, groupByFields);
            } else {
                if (queryWrapper instanceof DynamicJoinQueryWrapper) {
                    DynamicJoinQueryWrapper<TaskJobConfigCriteria, TaskJobConfig> dynamicQuery = (DynamicJoinQueryWrapper<
                            TaskJobConfigCriteria,
                            TaskJobConfig
                        >) queryWrapper;
                    dynamicQuery
                        .getAnnoJoiners()
                        .stream()
                        .filter(annoJoiner -> annoJoiner.getColumnName().equals(column) && annoJoiner.getFieldName().equals(key))
                        .findFirst()
                        .ifPresent(annoJoiner -> {
                            String alias = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, annoJoiner.getFieldName());
                            getAggregateAndGroupBy(filter, annoJoiner.getAlias() + "." + column, alias, selectFields, groupByFields);
                        });
                }
            }
        });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, TaskJobConfig.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
