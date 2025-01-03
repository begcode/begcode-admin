package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.repository.ResourceCategoryRepository;
import com.begcode.monolith.service.criteria.ResourceCategoryCriteria;
import com.begcode.monolith.service.dto.ResourceCategoryDTO;
import com.begcode.monolith.service.mapper.ResourceCategoryMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.CriteriaUtil;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link ResourceCategory}实体执行复杂查询的Service。
 * 主要输入是一个{@link ResourceCategoryCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ResourceCategoryDTO}列表{@link List} 或 {@link ResourceCategoryDTO} 的分页列表 {@link Page}。
 */
@Service
public class ResourceCategoryQueryService implements QueryService<ResourceCategory> {

    private static final Logger log = LoggerFactory.getLogger(ResourceCategoryQueryService.class);

    protected final ResourceCategoryRepository resourceCategoryRepository;

    protected final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryQueryService(
        ResourceCategoryRepository resourceCategoryRepository,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<ResourceCategoryDTO> findByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryMapper.toDto(Binder.joinQueryList(queryWrapper, ResourceCategory.class));
    }

    /**
     * Return a {@link IPage} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<ResourceCategoryDTO> findByCriteria(ResourceCategoryCriteria criteria, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, ResourceCategory.class, page).convert(resourceCategory -> {
            Binder.bindRelations(resourceCategory, new String[] { "children" });
            return resourceCategoryMapper.toDto(resourceCategory);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return resourceCategoryRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the resourceCategories for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<ResourceCategoryDTO> findAllTop(ResourceCategoryCriteria criteria, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, ResourceCategory.class, page).convert(resourceCategory -> {
            Binder.bindRelations(resourceCategory, new String[] { "parent" });
            return resourceCategoryMapper.toDto(resourceCategory);
        });
    }

    /**
     * Get all the resourceCategories for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ResourceCategoryDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ResourceCategories for parent is parentId");
        return resourceCategoryRepository
            .selectList(new LambdaUpdateWrapper<ResourceCategory>().eq(ResourceCategory::getParentId, parentId))
            .stream()
            .map(resourceCategory -> {
                Binder.bindRelations(resourceCategory, new String[] { "parent" });
                return resourceCategoryMapper.toDto(resourceCategory);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ResourceCategoryCriteria criteria) {
        return resourceCategoryRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ResourceCategoryCriteria criteria) {
        return resourceCategoryRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link ResourceCategoryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ResourceCategory> createQueryWrapper(ResourceCategoryCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            ResourceCategoryCriteria keywordsCriteria = new ResourceCategoryCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.orderNumber().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.title().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            ResourceCategoryCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<ResourceCategory> queryWrapper = new DynamicJoinQueryWrapper<>(ResourceCategoryCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, ResourceCategory.class);
    }

    /**
     * Function to convert {@link ResourceCategoryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ResourceCategory> createQueryWrapperNoJoin(ResourceCategoryCriteria criteria) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, ResourceCategory.class);
    }

    /**
     * Return a {@link IPage} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<ResourceCategoryDTO> findByQueryWrapper(QueryWrapper<ResourceCategory> queryWrapper, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return resourceCategoryRepository.selectPage(page, queryWrapper).convert(resourceCategoryMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(ResourceCategoryCriteria criteria) {
        QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.title", criteria.getTitle());
        fieldNameMap.put("self.code", criteria.getCode());
        fieldNameMap.put("self.order_number", criteria.getOrderNumber());
        fieldNameMap.put("self.parent_id", criteria.getParentId());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, ResourceCategory.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
