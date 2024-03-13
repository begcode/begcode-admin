package com.begcode.monolith.service;

import static com.diboot.core.binding.QueryBuilder.criteriaToWrapper;
import static com.diboot.core.binding.QueryBuilder.criteriaToWrapperNoJoin;
import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.*; // for static metamodels
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.service.criteria.ApiPermissionCriteria;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import com.begcode.monolith.service.mapper.ApiPermissionMapper;
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
 * 用于对数据库中的{@link ApiPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ApiPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ApiPermissionDTO}列表{@link List} 或 {@link ApiPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
public class ApiPermissionQueryService implements QueryService<ApiPermission> {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionQueryService.class);

    protected final ApiPermissionRepository apiPermissionRepository;

    protected final ApiPermissionMapper apiPermissionMapper;

    public ApiPermissionQueryService(ApiPermissionRepository apiPermissionRepository, ApiPermissionMapper apiPermissionMapper) {
        this.apiPermissionRepository = apiPermissionRepository;
        this.apiPermissionMapper = apiPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionMapper.toDto(Binder.joinQueryList(queryWrapper, ApiPermission.class));
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, ApiPermission.class, page)
            .convert(apiPermission -> {
                Binder.bindRelations(apiPermission, new String[] { "children" });
                return apiPermissionMapper.toDto(apiPermission);
            });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(ApiPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return apiPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the apiPermissions for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<ApiPermissionDTO> findAllTop(ApiPermissionCriteria criteria, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, ApiPermission.class, page)
            .convert(apiPermission -> {
                Binder.bindRelations(apiPermission, new String[] { "parent" });
                return apiPermissionMapper.toDto(apiPermission);
            });
    }

    /**
     * Get all the apiPermissions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ApiPermissionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ApiPermissions for parent is parentId");
        return apiPermissionRepository
            .selectList(new LambdaUpdateWrapper<ApiPermission>().eq(ApiPermission::getParentId, parentId))
            .stream()
            .map(apiPermission -> {
                Binder.bindRelations(apiPermission, new String[] { "parent" });
                return apiPermissionMapper.toDto(apiPermission);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return (List<T>) apiPermissionRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return apiPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link ApiPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ApiPermission> createQueryWrapper(ApiPermissionCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            ApiPermissionCriteria keywordsCriteria = new ApiPermissionCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.serviceName().setContains(keywords);
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            keywordsCriteria.method().setContains(keywords);
            keywordsCriteria.url().setContains(keywords);
            ApiPermissionCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<ApiPermission> queryWrapper = new DynamicJoinQueryWrapper<>(ApiPermissionCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link ApiPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ApiPermission> createQueryWrapperNoJoin(ApiPermissionCriteria criteria) {
        QueryWrapper<ApiPermission> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<ApiPermission> createQueryWrapper(
        QueryWrapper<ApiPermission> queryWrapper,
        Boolean useOr,
        ApiPermissionCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<ApiPermission>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, ApiPermission.class);
            Map.Entry<QueryWrapper<ApiPermission>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<ApiPermission> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<ApiPermission> createQueryWrapperNoJoin(
        QueryWrapper<ApiPermission> queryWrapper,
        Boolean useOr,
        ApiPermissionCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<ApiPermission>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(
                criteria,
                ApiPermission.class
            );
            Map.Entry<QueryWrapper<ApiPermission>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<ApiPermission> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<ApiPermissionDTO> findByQueryWrapper(QueryWrapper<ApiPermission> queryWrapper, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return apiPermissionRepository.selectPage(page, queryWrapper).convert(apiPermissionMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(ApiPermissionCriteria criteria) {
        QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getServiceName() != null) {
            getAggregateAndGroupBy(criteria.getServiceName(), "service_name", selectFields, groupByFields);
        }
        if (criteria.getName() != null) {
            getAggregateAndGroupBy(criteria.getName(), "name", selectFields, groupByFields);
        }
        if (criteria.getCode() != null) {
            getAggregateAndGroupBy(criteria.getCode(), "code", selectFields, groupByFields);
        }
        if (criteria.getDescription() != null) {
            getAggregateAndGroupBy(criteria.getDescription(), "description", selectFields, groupByFields);
        }
        if (criteria.getType() != null) {
            getAggregateAndGroupBy(criteria.getType(), "type", selectFields, groupByFields);
        }
        if (criteria.getMethod() != null) {
            getAggregateAndGroupBy(criteria.getMethod(), "method", selectFields, groupByFields);
        }
        if (criteria.getUrl() != null) {
            getAggregateAndGroupBy(criteria.getUrl(), "url", selectFields, groupByFields);
        }
        if (criteria.getStatus() != null) {
            getAggregateAndGroupBy(criteria.getStatus(), "status", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return apiPermissionRepository.selectMaps(queryWrapper);
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
