package com.begcode.monolith.service;

import static com.diboot.core.binding.QueryBuilder.criteriaToWrapper;
import static com.diboot.core.binding.QueryBuilder.criteriaToWrapperNoJoin;
import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.*; // for static metamodels
import com.begcode.monolith.domain.UReportFile;
import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.criteria.UReportFileCriteria;
import com.begcode.monolith.service.dto.UReportFileDTO;
import com.begcode.monolith.service.mapper.UReportFileMapper;
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
 * 用于对数据库中的{@link UReportFile}实体执行复杂查询的Service。
 * 主要输入是一个{@link UReportFileCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UReportFileDTO}列表{@link List} 或 {@link UReportFileDTO} 的分页列表 {@link Page}。
 */
@Service
public class UReportFileQueryService implements QueryService<UReportFile> {

    private final Logger log = LoggerFactory.getLogger(UReportFileQueryService.class);

    protected final UReportFileRepository uReportFileRepository;

    protected final UReportFileMapper uReportFileMapper;

    public UReportFileQueryService(UReportFileRepository uReportFileRepository, UReportFileMapper uReportFileMapper) {
        this.uReportFileRepository = uReportFileRepository;
        this.uReportFileMapper = uReportFileMapper;
    }

    /**
     * Return a {@link List} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<UReportFileDTO> findByCriteria(UReportFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return uReportFileMapper.toDto(Binder.joinQueryList(queryWrapper, UReportFile.class));
    }

    /**
     * Return a {@link IPage} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<UReportFileDTO> findByCriteria(UReportFileCriteria criteria, Page<UReportFile> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return Binder
            .joinQueryPage(queryWrapper, UReportFile.class, page)
            .convert(uReportFile -> {
                Binder.bindRelations(uReportFile, new String[] {});
                return uReportFileMapper.toDto(uReportFile);
            });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(UReportFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return uReportFileRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UReportFileCriteria criteria) {
        return (List<T>) uReportFileRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UReportFileCriteria criteria) {
        return uReportFileRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link UReportFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UReportFile> createQueryWrapper(UReportFileCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            UReportFileCriteria keywordsCriteria = new UReportFileCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            UReportFileCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<UReportFile> queryWrapper = new DynamicJoinQueryWrapper<>(UReportFileCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link UReportFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UReportFile> createQueryWrapperNoJoin(UReportFileCriteria criteria) {
        QueryWrapper<UReportFile> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<UReportFile> createQueryWrapper(
        QueryWrapper<UReportFile> queryWrapper,
        Boolean useOr,
        UReportFileCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<UReportFile>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, UReportFile.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<UReportFile>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<UReportFile> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<UReportFile> createQueryWrapperNoJoin(
        QueryWrapper<UReportFile> queryWrapper,
        Boolean useOr,
        UReportFileCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<UReportFile>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(criteria, UReportFile.class);
            if (MapUtils.isNotEmpty(queryWrapperMapMap)) {
                Map.Entry<QueryWrapper<UReportFile>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get();
                Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
                if (MapUtils.isNotEmpty(fieldMap)) {
                    if (queryWrapper == null) {
                        queryWrapper = queryWrapperMapEntry.getKey();
                    }
                    QueryWrapper<UReportFile> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<UReportFileDTO> findByQueryWrapper(QueryWrapper<UReportFile> queryWrapper, Page<UReportFile> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uReportFileRepository.selectPage(page, queryWrapper).convert(uReportFileMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(UReportFileCriteria criteria) {
        QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getName() != null) {
            getAggregateAndGroupBy(criteria.getName(), "name", selectFields, groupByFields);
        }
        if (criteria.getCreateAt() != null) {
            getAggregateAndGroupBy(criteria.getCreateAt(), "create_at", selectFields, groupByFields);
        }
        if (criteria.getUpdateAt() != null) {
            getAggregateAndGroupBy(criteria.getUpdateAt(), "update_at", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return uReportFileRepository.selectMaps(queryWrapper);
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
