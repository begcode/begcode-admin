package com.begcode.monolith.service;

import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.repository.ResourceCategoryRepository;
import com.begcode.monolith.service.base.ResourceCategoryBaseService;
import com.begcode.monolith.service.mapper.ResourceCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ResourceCategory}.
 */
@Service
public class ResourceCategoryService extends ResourceCategoryBaseService<ResourceCategoryRepository, ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryService.class);

    public ResourceCategoryService(
        ResourceCategoryRepository resourceCategoryRepository,
        CacheManager cacheManager,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        super(resourceCategoryRepository, cacheManager, resourceCategoryMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
