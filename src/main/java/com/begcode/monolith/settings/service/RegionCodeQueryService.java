package com.begcode.monolith.settings.service;

import static com.diboot.core.binding.QueryBuilder.criteriaToWrapper;
import static com.diboot.core.binding.QueryBuilder.criteriaToWrapperNoJoin;
import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.*; // for static metamodels
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.service.criteria.RegionCodeCriteria;
import com.begcode.monolith.settings.service.dto.RegionCodeDTO;
import com.begcode.monolith.settings.service.mapper.RegionCodeMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import java.util.stream.Collectors;
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
 * 用于对数据库中的{@link RegionCode}实体执行复杂查询的Service。
 * 主要输入是一个{@link RegionCodeCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link RegionCodeDTO}列表{@link List} 或 {@link RegionCodeDTO} 的分页列表 {@link Page}。
 */
@Service
public class RegionCodeQueryService implements QueryService<RegionCode> {

    private final Logger log = LoggerFactory.getLogger(RegionCodeQueryService.class);

    protected final RegionCodeRepository regionCodeRepository;

    protected final RegionCodeMapper regionCodeMapper;

    public RegionCodeQueryService(RegionCodeRepository regionCodeRepository, RegionCodeMapper regionCodeMapper) {
        this.regionCodeRepository = regionCodeRepository;
        this.regionCodeMapper = regionCodeMapper;
    }

    /**
     * Return a {@link List} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<RegionCodeDTO> findByCriteria(RegionCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return regionCodeMapper.toDto(Binder.joinQueryList(queryWrapper, RegionCode.class));
    }

    /**
     * Return a {@link IPage} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<RegionCodeDTO> findByCriteria(RegionCodeCriteria criteria, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, RegionCode.class, page)
            .convert(regionCode -> {
                Binder.bindRelations(regionCode, new String[] { "children" });
                return regionCodeMapper.toDto(regionCode);
            });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(RegionCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return regionCodeRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the regionCodes for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<RegionCodeDTO> findAllTop(RegionCodeCriteria criteria, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, RegionCode.class, page)
            .convert(regionCode -> {
                Binder.bindRelations(regionCode, new String[] { "parent" });
                return regionCodeMapper.toDto(regionCode);
            });
    }

    /**
     * Get all the regionCodes for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<RegionCodeDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all RegionCodes for parent is parentId");
        return regionCodeRepository
            .selectList(new LambdaUpdateWrapper<RegionCode>().eq(RegionCode::getParentId, parentId))
            .stream()
            .map(regionCode -> {
                Binder.bindRelations(regionCode, new String[] { "parent" });
                return regionCodeMapper.toDto(regionCode);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, RegionCodeCriteria criteria) {
        return (List<T>) regionCodeRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, RegionCodeCriteria criteria) {
        return regionCodeRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link RegionCodeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<RegionCode> createQueryWrapper(RegionCodeCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            RegionCodeCriteria keywordsCriteria = new RegionCodeCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lng().setEquals(Double.valueOf(keywords));
                keywordsCriteria.lat().setEquals(Double.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.areaCode().setContains(keywords);
            keywordsCriteria.cityCode().setContains(keywords);
            keywordsCriteria.mergerName().setContains(keywords);
            keywordsCriteria.shortName().setContains(keywords);
            keywordsCriteria.zipCode().setContains(keywords);
            RegionCodeCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<RegionCode> queryWrapper = new DynamicJoinQueryWrapper<>(RegionCodeCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link RegionCodeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<RegionCode> createQueryWrapperNoJoin(RegionCodeCriteria criteria) {
        QueryWrapper<RegionCode> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<RegionCode> createQueryWrapper(QueryWrapper<RegionCode> queryWrapper, Boolean useOr, RegionCodeCriteria criteria) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<RegionCode>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, RegionCode.class);
            Map.Entry<QueryWrapper<RegionCode>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<RegionCode> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<RegionCode> createQueryWrapperNoJoin(
        QueryWrapper<RegionCode> queryWrapper,
        Boolean useOr,
        RegionCodeCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<RegionCode>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(criteria, RegionCode.class);
            Map.Entry<QueryWrapper<RegionCode>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<RegionCode> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<RegionCodeDTO> findByQueryWrapper(QueryWrapper<RegionCode> queryWrapper, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return regionCodeRepository.selectPage(page, queryWrapper).convert(regionCodeMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(RegionCodeCriteria criteria) {
        QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getName() != null) {
            getAggregateAndGroupBy(criteria.getName(), "name", selectFields, groupByFields);
        }
        if (criteria.getAreaCode() != null) {
            getAggregateAndGroupBy(criteria.getAreaCode(), "area_code", selectFields, groupByFields);
        }
        if (criteria.getCityCode() != null) {
            getAggregateAndGroupBy(criteria.getCityCode(), "city_code", selectFields, groupByFields);
        }
        if (criteria.getMergerName() != null) {
            getAggregateAndGroupBy(criteria.getMergerName(), "merger_name", selectFields, groupByFields);
        }
        if (criteria.getShortName() != null) {
            getAggregateAndGroupBy(criteria.getShortName(), "short_name", selectFields, groupByFields);
        }
        if (criteria.getZipCode() != null) {
            getAggregateAndGroupBy(criteria.getZipCode(), "zip_code", selectFields, groupByFields);
        }
        if (criteria.getLevel() != null) {
            getAggregateAndGroupBy(criteria.getLevel(), "level", selectFields, groupByFields);
        }
        if (criteria.getLng() != null) {
            getAggregateAndGroupBy(criteria.getLng(), "lng", selectFields, groupByFields);
        }
        if (criteria.getLat() != null) {
            getAggregateAndGroupBy(criteria.getLat(), "lat", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return regionCodeRepository.selectMaps(queryWrapper);
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
