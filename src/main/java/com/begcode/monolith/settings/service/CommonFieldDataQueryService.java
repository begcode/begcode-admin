package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.repository.CommonFieldDataRepository;
import com.begcode.monolith.settings.service.criteria.CommonFieldDataCriteria;
import com.begcode.monolith.settings.service.dto.CommonFieldDataDTO;
import com.begcode.monolith.settings.service.mapper.CommonFieldDataMapper;
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
 * 用于对数据库中的{@link CommonFieldData}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonFieldDataCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonFieldDataDTO}列表{@link List} 或 {@link CommonFieldDataDTO} 的分页列表 {@link Page}。
 */
@Service
public class CommonFieldDataQueryService implements QueryService<CommonFieldData> {

    private static final Logger log = LoggerFactory.getLogger(CommonFieldDataQueryService.class);

    protected final CommonFieldDataRepository commonFieldDataRepository;

    protected final CommonFieldDataMapper commonFieldDataMapper;

    public CommonFieldDataQueryService(CommonFieldDataRepository commonFieldDataRepository, CommonFieldDataMapper commonFieldDataMapper) {
        this.commonFieldDataRepository = commonFieldDataRepository;
        this.commonFieldDataMapper = commonFieldDataMapper;
    }

    /**
     * Return a {@link List} of {@link CommonFieldDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<CommonFieldDataDTO> findByCriteria(CommonFieldDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonFieldData> queryWrapper = createQueryWrapper(criteria);
        return commonFieldDataMapper.toDto(Binder.joinQueryList(queryWrapper, CommonFieldData.class));
    }

    /**
     * Return a {@link IPage} of {@link CommonFieldDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<CommonFieldDataDTO> findByCriteria(CommonFieldDataCriteria criteria, Page<CommonFieldData> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<CommonFieldData> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, CommonFieldData.class, page).convert(commonFieldData -> {
            Binder.bindRelations(commonFieldData, new String[] {});
            return commonFieldDataMapper.toDto(commonFieldData);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(CommonFieldDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return commonFieldDataRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, CommonFieldDataCriteria criteria) {
        return commonFieldDataRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, CommonFieldDataCriteria criteria) {
        return commonFieldDataRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link CommonFieldDataCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<CommonFieldData> createQueryWrapper(CommonFieldDataCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            CommonFieldDataCriteria keywordsCriteria = new CommonFieldDataCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortValue().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.ownerEntityId().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.value().setContains(keywords);
            keywordsCriteria.label().setContains(keywords);
            keywordsCriteria.remark().setContains(keywords);
            keywordsCriteria.ownerEntityName().setContains(keywords);
            CommonFieldDataCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<CommonFieldData> queryWrapper = new DynamicJoinQueryWrapper<>(CommonFieldDataCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, CommonFieldData.class);
    }

    /**
     * Function to convert {@link CommonFieldDataCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<CommonFieldData> createQueryWrapperNoJoin(CommonFieldDataCriteria criteria) {
        QueryWrapper<CommonFieldData> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, CommonFieldData.class);
    }

    /**
     * Return a {@link IPage} of {@link CommonFieldDataDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<CommonFieldDataDTO> findByQueryWrapper(QueryWrapper<CommonFieldData> queryWrapper, Page<CommonFieldData> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonFieldDataRepository.selectPage(page, queryWrapper).convert(commonFieldDataMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(CommonFieldDataCriteria criteria) {
        QueryWrapper<CommonFieldData> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<CommonFieldDataCriteria, CommonFieldData> dynamicQuery = (DynamicJoinQueryWrapper<
                            CommonFieldDataCriteria,
                            CommonFieldData
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
            return Binder.joinQueryMapsPage(queryWrapper, CommonFieldData.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
