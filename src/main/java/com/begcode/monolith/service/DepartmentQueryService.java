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
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.service.criteria.DepartmentCriteria;
import com.begcode.monolith.service.dto.DepartmentDTO;
import com.begcode.monolith.service.mapper.DepartmentMapper;
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
 * 用于对数据库中的{@link Department}实体执行复杂查询的Service。
 * 主要输入是一个{@link DepartmentCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DepartmentDTO}列表{@link List} 或 {@link DepartmentDTO} 的分页列表 {@link Page}。
 */
@Service
public class DepartmentQueryService implements QueryService<Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentQueryService.class);

    protected final DepartmentRepository departmentRepository;

    protected final DepartmentMapper departmentMapper;

    public DepartmentQueryService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<DepartmentDTO> findByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return departmentMapper.toDto(Binder.joinQueryList(queryWrapper, Department.class));
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<DepartmentDTO> findByCriteria(DepartmentCriteria criteria, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, Department.class, page)
            .convert(department -> {
                Binder.bindRelations(department, new String[] { "children" });
                return departmentMapper.toDto(department);
            });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(DepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return departmentRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the departments for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<DepartmentDTO> findAllTop(DepartmentCriteria criteria, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, Department.class, page)
            .convert(department -> {
                Binder.bindRelations(department, new String[] { "parent" });
                return departmentMapper.toDto(department);
            });
    }

    /**
     * Get all the departments for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<DepartmentDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all Departments for parent is parentId");
        return departmentRepository
            .selectList(new LambdaUpdateWrapper<Department>().eq(Department::getParentId, parentId))
            .stream()
            .map(department -> {
                Binder.bindRelations(department, new String[] { "parent" });
                return departmentMapper.toDto(department);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DepartmentCriteria criteria) {
        return (List<T>) departmentRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DepartmentCriteria criteria) {
        return departmentRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Department> createQueryWrapper(DepartmentCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            DepartmentCriteria keywordsCriteria = new DepartmentCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createUserId().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.address().setContains(keywords);
            keywordsCriteria.phoneNum().setContains(keywords);
            keywordsCriteria.logo().setContains(keywords);
            keywordsCriteria.contact().setContains(keywords);
            DepartmentCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<Department> queryWrapper = new DynamicJoinQueryWrapper<>(DepartmentCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Department> createQueryWrapperNoJoin(DepartmentCriteria criteria) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<Department> createQueryWrapper(QueryWrapper<Department> queryWrapper, Boolean useOr, DepartmentCriteria criteria) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<Department>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, Department.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<Department>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<Department> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<Department> createQueryWrapperNoJoin(
        QueryWrapper<Department> queryWrapper,
        Boolean useOr,
        DepartmentCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<Department>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(criteria, Department.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<Department>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<Department> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<DepartmentDTO> findByQueryWrapper(QueryWrapper<Department> queryWrapper, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return departmentRepository.selectPage(page, queryWrapper).convert(departmentMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(DepartmentCriteria criteria) {
        QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
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
        if (criteria.getAddress() != null) {
            getAggregateAndGroupBy(criteria.getAddress(), "address", selectFields, groupByFields);
        }
        if (criteria.getPhoneNum() != null) {
            getAggregateAndGroupBy(criteria.getPhoneNum(), "phone_num", selectFields, groupByFields);
        }
        if (criteria.getLogo() != null) {
            getAggregateAndGroupBy(criteria.getLogo(), "logo", selectFields, groupByFields);
        }
        if (criteria.getContact() != null) {
            getAggregateAndGroupBy(criteria.getContact(), "contact", selectFields, groupByFields);
        }
        if (criteria.getCreateUserId() != null) {
            getAggregateAndGroupBy(criteria.getCreateUserId(), "create_user_id", selectFields, groupByFields);
        }
        if (criteria.getCreateTime() != null) {
            getAggregateAndGroupBy(criteria.getCreateTime(), "create_time", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return departmentRepository.selectMaps(queryWrapper);
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
