package com.begcode.monolith.system.service;

import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.repository.FormConfigRepository;
import com.begcode.monolith.system.service.base.FormConfigBaseService;
import com.begcode.monolith.system.service.mapper.FormConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link FormConfig}.
 */
@Service
public class FormConfigService extends FormConfigBaseService<FormConfigRepository, FormConfig> {

    private final Logger log = LoggerFactory.getLogger(FormConfigService.class);

    public FormConfigService(FormConfigRepository formConfigRepository, CacheManager cacheManager, FormConfigMapper formConfigMapper) {
        super(formConfigRepository, cacheManager, formConfigMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
