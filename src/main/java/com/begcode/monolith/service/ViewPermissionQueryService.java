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
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.service.criteria.ViewPermissionCriteria;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
import com.begcode.monolith.service.mapper.ViewPermissionMapper;
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
 * 用于对数据库中的{@link ViewPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ViewPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ViewPermissionDTO}列表{@link List} 或 {@link ViewPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
public class ViewPermissionQueryService implements QueryService<ViewPermission> {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionQueryService.class);

    protected final ViewPermissionRepository viewPermissionRepository;

    protected final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionQueryService(ViewPermissionRepository viewPermissionRepository, ViewPermissionMapper viewPermissionMapper) {
        this.viewPermissionRepository = viewPermissionRepository;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionMapper.toDto(Binder.joinQueryList(queryWrapper, ViewPermission.class));
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, ViewPermission.class, page)
            .convert(viewPermission -> {
                Binder.bindRelations(viewPermission, new String[] { "children", "authorities" });
                return viewPermissionMapper.toDto(viewPermission);
            });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(ViewPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return viewPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the viewPermissions for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<ViewPermissionDTO> findAllTop(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, ViewPermission.class, page)
            .convert(viewPermission -> {
                Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                return viewPermissionMapper.toDto(viewPermission);
            });
    }

    /**
     * Get all the viewPermissions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ViewPermissionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ViewPermissions for parent is parentId");
        return viewPermissionRepository
            .selectList(new LambdaUpdateWrapper<ViewPermission>().eq(ViewPermission::getParentId, parentId))
            .stream()
            .map(viewPermission -> {
                Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                return viewPermissionMapper.toDto(viewPermission);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return (List<T>) viewPermissionRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return viewPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ViewPermission> createQueryWrapper(ViewPermissionCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            ViewPermissionCriteria keywordsCriteria = new ViewPermissionCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.order().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.text().setContains(keywords);
            keywordsCriteria.i18n().setContains(keywords);
            keywordsCriteria.link().setContains(keywords);
            keywordsCriteria.externalLink().setContains(keywords);
            keywordsCriteria.icon().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            keywordsCriteria.apiPermissionCodes().setContains(keywords);
            keywordsCriteria.componentFile().setContains(keywords);
            keywordsCriteria.redirect().setContains(keywords);
            ViewPermissionCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<ViewPermission> queryWrapper = new DynamicJoinQueryWrapper<>(ViewPermissionCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ViewPermission> createQueryWrapperNoJoin(ViewPermissionCriteria criteria) {
        QueryWrapper<ViewPermission> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<ViewPermission> createQueryWrapper(
        QueryWrapper<ViewPermission> queryWrapper,
        Boolean useOr,
        ViewPermissionCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<ViewPermission>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, ViewPermission.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<ViewPermission>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<ViewPermission> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<ViewPermission> createQueryWrapperNoJoin(
        QueryWrapper<ViewPermission> queryWrapper,
        Boolean useOr,
        ViewPermissionCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<ViewPermission>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(
                criteria,
                ViewPermission.class
            );
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<ViewPermission>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<ViewPermission> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<ViewPermissionDTO> findByQueryWrapper(QueryWrapper<ViewPermission> queryWrapper, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(ViewPermissionCriteria criteria) {
        QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getText() != null) {
            getAggregateAndGroupBy(criteria.getText(), "text", selectFields, groupByFields);
        }
        if (criteria.getType() != null) {
            getAggregateAndGroupBy(criteria.getType(), "type", selectFields, groupByFields);
        }
        if (criteria.getI18n() != null) {
            getAggregateAndGroupBy(criteria.getI18n(), "i_18_n", selectFields, groupByFields);
        }
        if (criteria.getGroup() != null) {
            getAggregateAndGroupBy(criteria.getGroup(), "group", selectFields, groupByFields);
        }
        if (criteria.getLink() != null) {
            getAggregateAndGroupBy(criteria.getLink(), "link", selectFields, groupByFields);
        }
        if (criteria.getExternalLink() != null) {
            getAggregateAndGroupBy(criteria.getExternalLink(), "external_link", selectFields, groupByFields);
        }
        if (criteria.getTarget() != null) {
            getAggregateAndGroupBy(criteria.getTarget(), "target", selectFields, groupByFields);
        }
        if (criteria.getIcon() != null) {
            getAggregateAndGroupBy(criteria.getIcon(), "icon", selectFields, groupByFields);
        }
        if (criteria.getDisabled() != null) {
            getAggregateAndGroupBy(criteria.getDisabled(), "disabled", selectFields, groupByFields);
        }
        if (criteria.getHide() != null) {
            getAggregateAndGroupBy(criteria.getHide(), "hide", selectFields, groupByFields);
        }
        if (criteria.getHideInBreadcrumb() != null) {
            getAggregateAndGroupBy(criteria.getHideInBreadcrumb(), "hide_in_breadcrumb", selectFields, groupByFields);
        }
        if (criteria.getShortcut() != null) {
            getAggregateAndGroupBy(criteria.getShortcut(), "shortcut", selectFields, groupByFields);
        }
        if (criteria.getShortcutRoot() != null) {
            getAggregateAndGroupBy(criteria.getShortcutRoot(), "shortcut_root", selectFields, groupByFields);
        }
        if (criteria.getReuse() != null) {
            getAggregateAndGroupBy(criteria.getReuse(), "reuse", selectFields, groupByFields);
        }
        if (criteria.getCode() != null) {
            getAggregateAndGroupBy(criteria.getCode(), "code", selectFields, groupByFields);
        }
        if (criteria.getDescription() != null) {
            getAggregateAndGroupBy(criteria.getDescription(), "description", selectFields, groupByFields);
        }
        if (criteria.getOrder() != null) {
            getAggregateAndGroupBy(criteria.getOrder(), "order", selectFields, groupByFields);
        }
        if (criteria.getApiPermissionCodes() != null) {
            getAggregateAndGroupBy(criteria.getApiPermissionCodes(), "api_permission_codes", selectFields, groupByFields);
        }
        if (criteria.getComponentFile() != null) {
            getAggregateAndGroupBy(criteria.getComponentFile(), "component_file", selectFields, groupByFields);
        }
        if (criteria.getRedirect() != null) {
            getAggregateAndGroupBy(criteria.getRedirect(), "redirect", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return viewPermissionRepository.selectMaps(queryWrapper);
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
