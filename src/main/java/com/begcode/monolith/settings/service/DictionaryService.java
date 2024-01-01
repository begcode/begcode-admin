package com.begcode.monolith.settings.service;

import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.repository.DictionaryRepository;
import com.begcode.monolith.settings.service.base.DictionaryBaseService;
import com.begcode.monolith.settings.service.mapper.DictionaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Dictionary}.
 */
@Service
public class DictionaryService extends DictionaryBaseService<DictionaryRepository, Dictionary> {

    private final Logger log = LoggerFactory.getLogger(DictionaryService.class);

    public DictionaryService(DictionaryRepository dictionaryRepository, CacheManager cacheManager, DictionaryMapper dictionaryMapper) {
        super(dictionaryRepository, cacheManager, dictionaryMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
