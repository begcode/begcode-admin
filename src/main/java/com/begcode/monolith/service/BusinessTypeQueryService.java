package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.repository.BusinessTypeRepository;
import com.begcode.monolith.service.criteria.BusinessTypeCriteria;
import com.begcode.monolith.service.dto.BusinessTypeDTO;
import com.begcode.monolith.service.mapper.BusinessTypeMapper;
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
 * 用于对数据库中的{@link BusinessType}实体执行复杂查询的Service。
 * 主要输入是一个{@link BusinessTypeCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link BusinessTypeDTO}列表{@link List} 或 {@link BusinessTypeDTO} 的分页列表 {@link Page}。
 */
@Service
public class BusinessTypeQueryService implements QueryService<BusinessType> {

    private static final Logger log = LoggerFactory.getLogger(BusinessTypeQueryService.class);

    protected final BusinessTypeRepository businessTypeRepository;

    protected final BusinessTypeMapper businessTypeMapper;

    public BusinessTypeQueryService(BusinessTypeRepository businessTypeRepository, BusinessTypeMapper businessTypeMapper) {
        this.businessTypeRepository = businessTypeRepository;
        this.businessTypeMapper = businessTypeMapper;
    }

    /**
     * Return a {@link List} of {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<BusinessTypeDTO> findByCriteria(BusinessTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
        return businessTypeMapper.toDto(Binder.joinQueryList(queryWrapper, BusinessType.class));
    }

    /**
     * Return a {@link IPage} of {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<BusinessTypeDTO> findByCriteria(BusinessTypeCriteria criteria, Page<BusinessType> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, BusinessType.class, page).convert(businessType -> {
            Binder.bindRelations(businessType, new String[] {});
            return businessTypeMapper.toDto(businessType);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(BusinessTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return businessTypeRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, BusinessTypeCriteria criteria) {
        return businessTypeRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, BusinessTypeCriteria criteria) {
        return businessTypeRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link BusinessTypeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<BusinessType> createQueryWrapper(BusinessTypeCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            BusinessTypeCriteria keywordsCriteria = new BusinessTypeCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            keywordsCriteria.icon().setContains(keywords);
            BusinessTypeCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<BusinessType> queryWrapper = new DynamicJoinQueryWrapper<>(BusinessTypeCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, BusinessType.class);
    }

    /**
     * Function to convert {@link BusinessTypeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<BusinessType> createQueryWrapperNoJoin(BusinessTypeCriteria criteria) {
        QueryWrapper<BusinessType> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, BusinessType.class);
    }

    /**
     * Return a {@link IPage} of {@link BusinessTypeDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<BusinessTypeDTO> findByQueryWrapper(QueryWrapper<BusinessType> queryWrapper, Page<BusinessType> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return businessTypeRepository.selectPage(page, queryWrapper).convert(businessTypeMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(BusinessTypeCriteria criteria) {
        QueryWrapper<BusinessType> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<BusinessTypeCriteria, BusinessType> dynamicQuery = (DynamicJoinQueryWrapper<
                            BusinessTypeCriteria,
                            BusinessType
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
            return Binder.joinQueryMapsPage(queryWrapper, BusinessType.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
