package com.begcode.monolith.service;

import static com.diboot.core.binding.QueryBuilder.criteriaToWrapper;
import static com.diboot.core.binding.QueryBuilder.criteriaToWrapperNoJoin;
import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.*; // for static metamodels
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.repository.AuthorityRepository;
import com.begcode.monolith.service.criteria.AuthorityCriteria;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.mapper.AuthorityMapper;
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
 * 用于对数据库中的{@link Authority}实体执行复杂查询的Service。
 * 主要输入是一个{@link AuthorityCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AuthorityDTO}列表{@link List} 或 {@link AuthorityDTO} 的分页列表 {@link Page}。
 */
@Service
public class AuthorityQueryService implements QueryService<Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityQueryService.class);

    protected final AuthorityRepository authorityRepository;

    protected final AuthorityMapper authorityMapper;

    public AuthorityQueryService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Return a {@link List} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<AuthorityDTO> findByCriteria(AuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityMapper.toDto(Binder.joinQueryList(queryWrapper, Authority.class));
    }

    /**
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<AuthorityDTO> findByCriteria(AuthorityCriteria criteria, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, Authority.class, page)
            .convert(authority -> {
                Binder.bindRelations(authority, new String[] { "children", "department" });
                return authorityMapper.toDto(authority);
            });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(AuthorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return authorityRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the authorities for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<AuthorityDTO> findAllTop(AuthorityCriteria criteria, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, Authority.class, page)
            .convert(authority -> {
                Binder.bindRelations(authority, new String[] { "parent", "department" });
                return authorityMapper.toDto(authority);
            });
    }

    /**
     * Get all the authorities for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<AuthorityDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all Authorities for parent is parentId");
        return authorityRepository
            .selectList(new LambdaUpdateWrapper<Authority>().eq(Authority::getParentId, parentId))
            .stream()
            .map(authority -> {
                Binder.bindRelations(authority, new String[] { "parent", "department" });
                return authorityMapper.toDto(authority);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AuthorityCriteria criteria) {
        return (List<T>) authorityRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AuthorityCriteria criteria) {
        return authorityRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link AuthorityCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Authority> createQueryWrapper(AuthorityCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            AuthorityCriteria keywordsCriteria = new AuthorityCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.order().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.info().setContains(keywords);
            AuthorityCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<Authority> queryWrapper = new DynamicJoinQueryWrapper<>(AuthorityCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link AuthorityCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Authority> createQueryWrapperNoJoin(AuthorityCriteria criteria) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<Authority> createQueryWrapper(QueryWrapper<Authority> queryWrapper, Boolean useOr, AuthorityCriteria criteria) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<Authority>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, Authority.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<Authority>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<Authority> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<Authority> createQueryWrapperNoJoin(
        QueryWrapper<Authority> queryWrapper,
        Boolean useOr,
        AuthorityCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<Authority>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(criteria, Authority.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<Authority>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<Authority> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<AuthorityDTO> findByQueryWrapper(QueryWrapper<Authority> queryWrapper, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(AuthorityCriteria criteria) {
        QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getName() != null) {
            getAggregateAndGroupBy(criteria.getName(), "name", selectFields, groupByFields);
        }
        if (criteria.getCode() != null) {
            getAggregateAndGroupBy(criteria.getCode(), "code", selectFields, groupByFields);
        }
        if (criteria.getInfo() != null) {
            getAggregateAndGroupBy(criteria.getInfo(), "info", selectFields, groupByFields);
        }
        if (criteria.getOrder() != null) {
            getAggregateAndGroupBy(criteria.getOrder(), "order", selectFields, groupByFields);
        }
        if (criteria.getDisplay() != null) {
            getAggregateAndGroupBy(criteria.getDisplay(), "display", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return authorityRepository.selectMaps(queryWrapper);
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
