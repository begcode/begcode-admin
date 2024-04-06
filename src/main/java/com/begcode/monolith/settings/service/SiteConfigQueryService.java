package com.begcode.monolith.settings.service;

import static com.diboot.core.binding.QueryBuilder.criteriaToWrapper;
import static com.diboot.core.binding.QueryBuilder.criteriaToWrapperNoJoin;
import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.*; // for static metamodels
import com.begcode.monolith.settings.domain.SiteConfig;
import com.begcode.monolith.settings.repository.SiteConfigRepository;
import com.begcode.monolith.settings.service.criteria.SiteConfigCriteria;
import com.begcode.monolith.settings.service.dto.SiteConfigDTO;
import com.begcode.monolith.settings.service.mapper.SiteConfigMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.aggregate.*;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.RangeFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.mybatis.CriteriaUtil;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link SiteConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SiteConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SiteConfigDTO}列表{@link List} 或 {@link SiteConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class SiteConfigQueryService implements QueryService<SiteConfig> {

    private final Logger log = LoggerFactory.getLogger(SiteConfigQueryService.class);

    protected final SiteConfigRepository siteConfigRepository;

    protected final SiteConfigMapper siteConfigMapper;

    public SiteConfigQueryService(SiteConfigRepository siteConfigRepository, SiteConfigMapper siteConfigMapper) {
        this.siteConfigRepository = siteConfigRepository;
        this.siteConfigMapper = siteConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SiteConfigDTO> findByCriteria(SiteConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return siteConfigMapper.toDto(Binder.joinQueryList(queryWrapper, SiteConfig.class));
    }

    /**
     * Return a {@link IPage} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SiteConfigDTO> findByCriteria(SiteConfigCriteria criteria, Page<SiteConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SiteConfig.class, page).convert(siteConfig -> {
            Binder.bindRelations(siteConfig, new String[] {});
            return siteConfigMapper.toDto(siteConfig);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SiteConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return siteConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SiteConfigCriteria criteria) {
        return (List<T>) siteConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SiteConfigCriteria criteria) {
        return siteConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SiteConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SiteConfig> createQueryWrapper(SiteConfigCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SiteConfigCriteria keywordsCriteria = new SiteConfigCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortValue().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.categoryName().setContains(keywords);
            keywordsCriteria.categoryKey().setContains(keywords);
            SiteConfigCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<SiteConfig> queryWrapper = new DynamicJoinQueryWrapper<>(SiteConfigCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link SiteConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SiteConfig> createQueryWrapperNoJoin(SiteConfigCriteria criteria) {
        QueryWrapper<SiteConfig> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<SiteConfig> createQueryWrapper(QueryWrapper<SiteConfig> queryWrapper, Boolean useOr, SiteConfigCriteria criteria) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<SiteConfig>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, SiteConfig.class);
            Map.Entry<QueryWrapper<SiteConfig>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<SiteConfig> finalQueryWrapper = queryWrapper;
                Boolean finalUseOr = useOr;
                fieldMap.forEach((fieldName, filter) -> {
                    if (filter instanceof StringFilter) {
                        CriteriaUtil.build(
                            finalUseOr,
                            finalQueryWrapper,
                            buildStringSpecification((StringFilter) filter, fieldName, finalUseOr)
                        );
                    } else if (filter instanceof RangeFilter) {
                        CriteriaUtil.build(
                            finalUseOr,
                            finalQueryWrapper,
                            buildRangeSpecification((RangeFilter) filter, fieldName, finalUseOr)
                        );
                    } else if (filter instanceof Filter) {
                        CriteriaUtil.build(finalUseOr, finalQueryWrapper, buildSpecification((Filter) filter, fieldName, finalUseOr));
                    }
                });
            }
            if (criteria.getAnd() != null) {
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(criteria.getAnd(), false, true);
                if (
                    !((stringObjectMap.containsKey("useOr") && stringObjectMap.keySet().size() == 1) ||
                        ObjectUtils.isEmpty(stringObjectMap))
                ) {
                    if (queryWrapper != null) {
                        queryWrapper.and(q -> createQueryWrapper(q, criteria.getAnd().getUseOr(), criteria.getAnd()));
                    } else {
                        queryWrapper = createQueryWrapper(null, criteria.getAnd().getUseOr(), criteria.getAnd());
                    }
                }
            } else {
                if (criteria.getOr() != null) {
                    Map<String, Object> stringObjectMap = BeanUtil.beanToMap(criteria.getOr(), false, true);
                    if (
                        !((stringObjectMap.containsKey("useOr") && stringObjectMap.keySet().size() == 1) ||
                            ObjectUtils.isEmpty(stringObjectMap))
                    ) {
                        if (queryWrapper != null) {
                            queryWrapper.or(q -> createQueryWrapper(q, criteria.getOr().getUseOr(), criteria.getOr()));
                        } else {
                            queryWrapper = createQueryWrapper(null, criteria.getOr().getUseOr(), criteria.getOr());
                        }
                    }
                }
            }
        }
        return queryWrapper;
    }

    private QueryWrapper<SiteConfig> createQueryWrapperNoJoin(
        QueryWrapper<SiteConfig> queryWrapper,
        Boolean useOr,
        SiteConfigCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<SiteConfig>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(criteria, SiteConfig.class);
            Map.Entry<QueryWrapper<SiteConfig>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<SiteConfig> finalQueryWrapper = queryWrapper;
                Boolean finalUseOr = useOr;
                fieldMap.forEach((fieldName, filter) -> {
                    if (filter instanceof StringFilter) {
                        CriteriaUtil.build(
                            finalUseOr,
                            finalQueryWrapper,
                            buildStringSpecification((StringFilter) filter, fieldName, finalUseOr)
                        );
                    } else if (filter instanceof RangeFilter) {
                        CriteriaUtil.build(
                            finalUseOr,
                            finalQueryWrapper,
                            buildRangeSpecification((RangeFilter) filter, fieldName, finalUseOr)
                        );
                    } else if (filter instanceof Filter) {
                        CriteriaUtil.build(finalUseOr, finalQueryWrapper, buildSpecification((Filter) filter, fieldName, finalUseOr));
                    }
                });
            }
            if (criteria.getAnd() != null) {
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(criteria.getAnd(), false, true);
                if (
                    !((stringObjectMap.containsKey("useOr") && stringObjectMap.keySet().size() == 1) ||
                        ObjectUtils.isEmpty(stringObjectMap))
                ) {
                    if (queryWrapper != null) {
                        queryWrapper.and(q -> createQueryWrapperNoJoin(q, criteria.getAnd().getUseOr(), criteria.getAnd()));
                    } else {
                        queryWrapper = createQueryWrapperNoJoin(null, criteria.getAnd().getUseOr(), criteria.getAnd());
                    }
                }
            } else {
                if (criteria.getOr() != null) {
                    Map<String, Object> stringObjectMap = BeanUtil.beanToMap(criteria.getOr(), false, true);
                    if (
                        !((stringObjectMap.containsKey("useOr") && stringObjectMap.keySet().size() == 1) ||
                            ObjectUtils.isEmpty(stringObjectMap))
                    ) {
                        if (queryWrapper != null) {
                            queryWrapper.or(q -> createQueryWrapperNoJoin(q, criteria.getOr().getUseOr(), criteria.getOr()));
                        } else {
                            queryWrapper = createQueryWrapperNoJoin(null, criteria.getOr().getUseOr(), criteria.getOr());
                        }
                    }
                }
            }
        }
        return queryWrapper;
    }

    /**
     * Return a {@link IPage} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SiteConfigDTO> findByQueryWrapper(QueryWrapper<SiteConfig> queryWrapper, Page<SiteConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return siteConfigRepository.selectPage(page, queryWrapper).convert(siteConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SiteConfigCriteria criteria) {
        QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getCategoryName() != null) {
            getAggregateAndGroupBy(criteria.getCategoryName(), "category_name", selectFields, groupByFields);
        }
        if (criteria.getCategoryKey() != null) {
            getAggregateAndGroupBy(criteria.getCategoryKey(), "category_key", selectFields, groupByFields);
        }
        if (criteria.getDisabled() != null) {
            getAggregateAndGroupBy(criteria.getDisabled(), "disabled", selectFields, groupByFields);
        }
        if (criteria.getSortValue() != null) {
            getAggregateAndGroupBy(criteria.getSortValue(), "sort_value", selectFields, groupByFields);
        }
        if (criteria.getBuiltIn() != null) {
            getAggregateAndGroupBy(criteria.getBuiltIn(), "built_in", selectFields, groupByFields);
        }
        if (criteria.getCreatedBy() != null) {
            getAggregateAndGroupBy(criteria.getCreatedBy(), "created_by", selectFields, groupByFields);
        }
        if (criteria.getCreatedDate() != null) {
            getAggregateAndGroupBy(criteria.getCreatedDate(), "created_date", selectFields, groupByFields);
        }
        if (criteria.getLastModifiedBy() != null) {
            getAggregateAndGroupBy(criteria.getLastModifiedBy(), "last_modified_by", selectFields, groupByFields);
        }
        if (criteria.getLastModifiedDate() != null) {
            getAggregateAndGroupBy(criteria.getLastModifiedDate(), "last_modified_date", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return siteConfigRepository.selectMaps(queryWrapper);
        }
        return Collections.emptyList();
    }

    private void getAggregateAndGroupBy(Filter<?> filter, String fieldName, List<String> selects, List<String> groupBys) {
        if (filter.getAggregate() != null) {
            if (filter.getAggregate() instanceof NumberAggregate) {
                buildAggregate((NumberAggregate) filter.getAggregate(), fieldName, selects);
            } else {
                buildAggregate(filter.getAggregate(), fieldName, selects);
            }
        }
        if (filter.getGroupBy() != null) {
            if (filter.getGroupBy() instanceof DateTimeGroupBy) {
                buildGroupBy((DateTimeGroupBy) filter.getGroupBy(), fieldName, groupBys, selects);
            } else {
                buildGroupBy(filter.getGroupBy(), fieldName, groupBys, selects);
            }
        }
    }
}
