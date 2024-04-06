package com.begcode.monolith.service;

import static com.diboot.core.binding.QueryBuilder.criteriaToWrapper;
import static com.diboot.core.binding.QueryBuilder.criteriaToWrapperNoJoin;
import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.*; // for static metamodels
import com.begcode.monolith.domain.UploadImage;
import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.criteria.UploadImageCriteria;
import com.begcode.monolith.service.dto.UploadImageDTO;
import com.begcode.monolith.service.mapper.UploadImageMapper;
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
 * 用于对数据库中的{@link UploadImage}实体执行复杂查询的Service。
 * 主要输入是一个{@link UploadImageCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UploadImageDTO}列表{@link List} 或 {@link UploadImageDTO} 的分页列表 {@link Page}。
 */
@Service
public class UploadImageQueryService implements QueryService<UploadImage> {

    private final Logger log = LoggerFactory.getLogger(UploadImageQueryService.class);

    protected final UploadImageRepository uploadImageRepository;

    protected final UploadImageMapper uploadImageMapper;

    public UploadImageQueryService(UploadImageRepository uploadImageRepository, UploadImageMapper uploadImageMapper) {
        this.uploadImageRepository = uploadImageRepository;
        this.uploadImageMapper = uploadImageMapper;
    }

    /**
     * Return a {@link List} of {@link UploadImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<UploadImageDTO> findByCriteria(UploadImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UploadImage> queryWrapper = createQueryWrapper(criteria);
        return uploadImageMapper.toDto(Binder.joinQueryList(queryWrapper, UploadImage.class));
    }

    /**
     * Return a {@link IPage} of {@link UploadImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<UploadImageDTO> findByCriteria(UploadImageCriteria criteria, Page<UploadImage> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UploadImage> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, UploadImage.class, page).convert(uploadImage -> {
            Binder.bindRelations(uploadImage, new String[] {});
            return uploadImageMapper.toDto(uploadImage);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(UploadImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return uploadImageRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UploadImageCriteria criteria) {
        return (List<T>) uploadImageRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UploadImageCriteria criteria) {
        return uploadImageRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link UploadImageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UploadImage> createQueryWrapper(UploadImageCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            UploadImageCriteria keywordsCriteria = new UploadImageCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.ownerEntityId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.fileSize().setEquals(Long.valueOf(keywords));
                keywordsCriteria.referenceCount().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.url().setContains(keywords);
            keywordsCriteria.fullName().setContains(keywords);
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.ext().setContains(keywords);
            keywordsCriteria.type().setContains(keywords);
            keywordsCriteria.path().setContains(keywords);
            keywordsCriteria.folder().setContains(keywords);
            keywordsCriteria.ownerEntityName().setContains(keywords);
            keywordsCriteria.businessTitle().setContains(keywords);
            keywordsCriteria.businessDesc().setContains(keywords);
            keywordsCriteria.businessStatus().setContains(keywords);
            keywordsCriteria.smartUrl().setContains(keywords);
            keywordsCriteria.mediumUrl().setContains(keywords);
            UploadImageCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<UploadImage> queryWrapper = new DynamicJoinQueryWrapper<>(UploadImageCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
    }

    /**
     * Function to convert {@link UploadImageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UploadImage> createQueryWrapperNoJoin(UploadImageCriteria criteria) {
        QueryWrapper<UploadImage> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria);
    }

    private QueryWrapper<UploadImage> createQueryWrapper(
        QueryWrapper<UploadImage> queryWrapper,
        Boolean useOr,
        UploadImageCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<UploadImage>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapper(criteria, UploadImage.class);
            Map.Entry<QueryWrapper<UploadImage>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<UploadImage> finalQueryWrapper = queryWrapper;
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

    private QueryWrapper<UploadImage> createQueryWrapperNoJoin(
        QueryWrapper<UploadImage> queryWrapper,
        Boolean useOr,
        UploadImageCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            Map<QueryWrapper<UploadImage>, Map<String, Object>> queryWrapperMapMap = criteriaToWrapperNoJoin(criteria, UploadImage.class);
            Map.Entry<QueryWrapper<UploadImage>, Map<String, Object>> queryWrapperMapEntry = queryWrapperMapMap
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow();
            Map<String, Object> fieldMap = queryWrapperMapEntry.getValue();
            if (MapUtils.isNotEmpty(fieldMap)) {
                if (queryWrapper == null) {
                    queryWrapper = queryWrapperMapEntry.getKey();
                }
                QueryWrapper<UploadImage> finalQueryWrapper = queryWrapper;
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
     * Return a {@link IPage} of {@link UploadImageDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<UploadImageDTO> findByQueryWrapper(QueryWrapper<UploadImage> queryWrapper, Page<UploadImage> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uploadImageRepository.selectPage(page, queryWrapper).convert(uploadImageMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(UploadImageCriteria criteria) {
        QueryWrapper<UploadImage> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getUrl() != null) {
            getAggregateAndGroupBy(criteria.getUrl(), "url", selectFields, groupByFields);
        }
        if (criteria.getFullName() != null) {
            getAggregateAndGroupBy(criteria.getFullName(), "full_name", selectFields, groupByFields);
        }
        if (criteria.getName() != null) {
            getAggregateAndGroupBy(criteria.getName(), "name", selectFields, groupByFields);
        }
        if (criteria.getExt() != null) {
            getAggregateAndGroupBy(criteria.getExt(), "ext", selectFields, groupByFields);
        }
        if (criteria.getType() != null) {
            getAggregateAndGroupBy(criteria.getType(), "type", selectFields, groupByFields);
        }
        if (criteria.getPath() != null) {
            getAggregateAndGroupBy(criteria.getPath(), "path", selectFields, groupByFields);
        }
        if (criteria.getFolder() != null) {
            getAggregateAndGroupBy(criteria.getFolder(), "folder", selectFields, groupByFields);
        }
        if (criteria.getOwnerEntityName() != null) {
            getAggregateAndGroupBy(criteria.getOwnerEntityName(), "owner_entity_name", selectFields, groupByFields);
        }
        if (criteria.getOwnerEntityId() != null) {
            getAggregateAndGroupBy(criteria.getOwnerEntityId(), "owner_entity_id", selectFields, groupByFields);
        }
        if (criteria.getBusinessTitle() != null) {
            getAggregateAndGroupBy(criteria.getBusinessTitle(), "business_title", selectFields, groupByFields);
        }
        if (criteria.getBusinessDesc() != null) {
            getAggregateAndGroupBy(criteria.getBusinessDesc(), "business_desc", selectFields, groupByFields);
        }
        if (criteria.getBusinessStatus() != null) {
            getAggregateAndGroupBy(criteria.getBusinessStatus(), "business_status", selectFields, groupByFields);
        }
        if (criteria.getCreateAt() != null) {
            getAggregateAndGroupBy(criteria.getCreateAt(), "create_at", selectFields, groupByFields);
        }
        if (criteria.getFileSize() != null) {
            getAggregateAndGroupBy(criteria.getFileSize(), "file_size", selectFields, groupByFields);
        }
        if (criteria.getSmartUrl() != null) {
            getAggregateAndGroupBy(criteria.getSmartUrl(), "smart_url", selectFields, groupByFields);
        }
        if (criteria.getMediumUrl() != null) {
            getAggregateAndGroupBy(criteria.getMediumUrl(), "medium_url", selectFields, groupByFields);
        }
        if (criteria.getReferenceCount() != null) {
            getAggregateAndGroupBy(criteria.getReferenceCount(), "reference_count", selectFields, groupByFields);
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
            return uploadImageRepository.selectMaps(queryWrapper);
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
