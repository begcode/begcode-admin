package com.begcode.monolith.service;

import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.service.base.ViewPermissionBaseService;
import com.begcode.monolith.service.mapper.ViewPermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ViewPermission}.
 */
@Service
public class ViewPermissionService extends ViewPermissionBaseService<ViewPermissionRepository, ViewPermission> {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionService.class);

    public ViewPermissionService(
        ViewPermissionRepository viewPermissionRepository,
        CacheManager cacheManager,
        ViewPermissionMapper viewPermissionMapper
    ) {
        super(viewPermissionRepository, cacheManager, viewPermissionMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
