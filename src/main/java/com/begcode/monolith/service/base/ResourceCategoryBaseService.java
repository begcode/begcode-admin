package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.repository.ResourceCategoryRepository;
import com.begcode.monolith.service.dto.ResourceCategoryDTO;
import com.begcode.monolith.service.mapper.ResourceCategoryMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.ResourceCategory}.
 */
@SuppressWarnings("UnusedReturnValue")
public class ResourceCategoryBaseService<R extends ResourceCategoryRepository, E extends ResourceCategory>
    extends BaseServiceImpl<ResourceCategoryRepository, ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.domain.ResourceCategory.class.getName() + ".parent",
        com.begcode.monolith.domain.ResourceCategory.class.getName() + ".children",
        com.begcode.monolith.domain.UploadImage.class.getName() + ".category",
        com.begcode.monolith.domain.UploadFile.class.getName() + ".category"
    );
    private final List<String> relationNames = List.of("children", "parent", "images", "files");

    protected final ResourceCategoryRepository resourceCategoryRepository;

    protected final CacheManager cacheManager;

    protected final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryBaseService(
        ResourceCategoryRepository resourceCategoryRepository,
        CacheManager cacheManager,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.cacheManager = cacheManager;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Save a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ResourceCategoryDTO save(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to save ResourceCategory : {}", resourceCategoryDTO);
        ResourceCategory resourceCategory = resourceCategoryMapper.toEntity(resourceCategoryDTO);
        clearChildrenCache();

        this.saveOrUpdate(resourceCategory);
        return findOne(resourceCategory.getId()).orElseThrow();
    }

    /**
     * Update a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public ResourceCategoryDTO update(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to update ResourceCategory : {}", resourceCategoryDTO);
        ResourceCategory resourceCategory = resourceCategoryMapper.toEntity(resourceCategoryDTO);
        clearChildrenCache();

        this.saveOrUpdate(resourceCategory);
        return findOne(resourceCategory.getId()).orElseThrow();
    }

    /**
     * Partially update a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ResourceCategoryDTO> partialUpdate(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to partially update ResourceCategory : {}", resourceCategoryDTO);

        return resourceCategoryRepository
            .findById(resourceCategoryDTO.getId())
            .map(existingResourceCategory -> {
                resourceCategoryMapper.partialUpdate(existingResourceCategory, resourceCategoryDTO);

                return existingResourceCategory;
            })
            .map(tempResourceCategory -> {
                resourceCategoryRepository.save(tempResourceCategory);
                return resourceCategoryMapper.toDto(resourceCategoryRepository.selectById(tempResourceCategory.getId()));
            });
    }

    /**
     * Get all the resourceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ResourceCategoryDTO> findAll(Page<ResourceCategory> pageable) {
        log.debug("Request to get all ResourceCategories");
        return this.page(pageable).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Get one resourceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ResourceCategoryDTO> findOne(Long id) {
        log.debug("Request to get ResourceCategory : {}", id);
        return Optional.ofNullable(resourceCategoryRepository.selectById(id))
            .map(resourceCategory -> {
                Binder.bindRelations(resourceCategory);
                return resourceCategory;
            })
            .map(resourceCategoryMapper::toDto);
    }

    /**
     * Delete the resourceCategory by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete ResourceCategory : {}", id);

        ResourceCategory resourceCategory = resourceCategoryRepository.selectById(id);
        if (resourceCategory.getChildren() != null) {
            resourceCategory.getChildren().forEach(subResourceCategory -> subResourceCategory.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        resourceCategoryRepository.deleteById(id);
    }

    /**
     * Update specified field by resourceCategory
     */
    @Transactional
    public void updateBatch(ResourceCategoryDTO changeResourceCategoryDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<ResourceCategory> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeResourceCategoryDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<ResourceCategory> resourceCategoryList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(resourceCategoryList)) {
                resourceCategoryList.forEach(resourceCategory -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                resourceCategory,
                                relationName,
                                BeanUtil.getFieldValue(resourceCategoryMapper.toEntity(changeResourceCategoryDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(resourceCategory, relationshipNames);
                });
            }
        }
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.begcode.monolith.domain.ResourceCategory.class.getName() + ".children")).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            ResourceCategory byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
