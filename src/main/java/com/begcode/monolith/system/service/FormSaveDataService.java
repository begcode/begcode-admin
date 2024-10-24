package com.begcode.monolith.system.service;

import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.base.FormSaveDataBaseService;
import com.begcode.monolith.system.service.mapper.FormSaveDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link FormSaveData}.
 */
@Service
public class FormSaveDataService extends FormSaveDataBaseService<FormSaveDataRepository, FormSaveData> {

    private final Logger log = LoggerFactory.getLogger(FormSaveDataService.class);

    public FormSaveDataService(
        FormSaveDataRepository formSaveDataRepository,
        CacheManager cacheManager,
        FormSaveDataMapper formSaveDataMapper
    ) {
        super(formSaveDataRepository, cacheManager, formSaveDataMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
