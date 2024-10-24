package com.begcode.monolith.settings.service;

import com.begcode.monolith.settings.domain.SystemConfig;
import com.begcode.monolith.settings.repository.SystemConfigRepository;
import com.begcode.monolith.settings.service.base.SystemConfigBaseService;
import com.begcode.monolith.settings.service.mapper.SystemConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link SystemConfig}.
 */
@Service
public class SystemConfigService extends SystemConfigBaseService<SystemConfigRepository, SystemConfig> {

    private final Logger log = LoggerFactory.getLogger(SystemConfigService.class);

    public SystemConfigService(
        SystemConfigRepository systemConfigRepository,
        CacheManager cacheManager,
        SystemConfigMapper systemConfigMapper
    ) {
        super(systemConfigRepository, cacheManager, systemConfigMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
