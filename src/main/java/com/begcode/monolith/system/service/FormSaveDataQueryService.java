package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.criteria.FormSaveDataCriteria;
import com.begcode.monolith.system.service.dto.FormSaveDataDTO;
import com.begcode.monolith.system.service.mapper.FormSaveDataMapper;
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
 * 用于对数据库中的{@link FormSaveData}实体执行复杂查询的Service。
 * 主要输入是一个{@link FormSaveDataCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link FormSaveDataDTO}列表{@link List} 或 {@link FormSaveDataDTO} 的分页列表 {@link Page}。
 */
@Service
public class FormSaveDataQueryService implements QueryService<FormSaveData> {

    private static final Logger log = LoggerFactory.getLogger(FormSaveDataQueryService.class);

    protected final FormSaveDataRepository formSaveDataRepository;

    protected final FormSaveDataMapper formSaveDataMapper;

    public FormSaveDataQueryService(FormSaveDataRepository formSaveDataRepository, FormSaveDataMapper formSaveDataMapper) {
        this.formSaveDataRepository = formSaveDataRepository;
        this.formSaveDataMapper = formSaveDataMapper;
    }

    /**
     * Return a {@link List} of {@link FormSaveDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<FormSaveDataDTO> findByCriteria(FormSaveDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<FormSaveData> queryWrapper = createQueryWrapper(criteria);
        return formSaveDataMapper.toDto(Binder.joinQueryList(queryWrapper, FormSaveData.class));
    }

    /**
     * Return a {@link IPage} of {@link FormSaveDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<FormSaveDataDTO> findByCriteria(FormSaveDataCriteria criteria, Page<FormSaveData> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<FormSaveData> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, FormSaveData.class, page).convert(formSaveData -> {
            Binder.bindRelations(formSaveData, new String[] {});
            return formSaveDataMapper.toDto(formSaveData);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(FormSaveDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return formSaveDataRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, FormSaveDataCriteria criteria) {
        return formSaveDataRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, FormSaveDataCriteria criteria) {
        return formSaveDataRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link FormSaveDataCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<FormSaveData> createQueryWrapper(FormSaveDataCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            FormSaveDataCriteria keywordsCriteria = new FormSaveDataCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            FormSaveDataCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<FormSaveData> queryWrapper = new DynamicJoinQueryWrapper<>(FormSaveDataCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, FormSaveData.class);
    }

    /**
     * Function to convert {@link FormSaveDataCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<FormSaveData> createQueryWrapperNoJoin(FormSaveDataCriteria criteria) {
        QueryWrapper<FormSaveData> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, FormSaveData.class);
    }

    /**
     * Return a {@link IPage} of {@link FormSaveDataDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<FormSaveDataDTO> findByQueryWrapper(QueryWrapper<FormSaveData> queryWrapper, Page<FormSaveData> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return formSaveDataRepository.selectPage(page, queryWrapper).convert(formSaveDataMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(FormSaveDataCriteria criteria) {
        QueryWrapper<FormSaveData> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<FormSaveDataCriteria, FormSaveData> dynamicQuery = (DynamicJoinQueryWrapper<
                            FormSaveDataCriteria,
                            FormSaveData
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
            return Binder.joinQueryMapsPage(queryWrapper, FormSaveData.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
