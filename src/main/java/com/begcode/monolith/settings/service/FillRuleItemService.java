package com.begcode.monolith.settings.service;

import com.begcode.monolith.settings.domain.FillRuleItem;
import com.begcode.monolith.settings.repository.FillRuleItemRepository;
import com.begcode.monolith.settings.service.base.FillRuleItemBaseService;
import com.begcode.monolith.settings.service.mapper.FillRuleItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link FillRuleItem}.
 */
@Service
public class FillRuleItemService extends FillRuleItemBaseService<FillRuleItemRepository, FillRuleItem> {

    private final Logger log = LoggerFactory.getLogger(FillRuleItemService.class);

    public FillRuleItemService(
        FillRuleItemRepository fillRuleItemRepository,
        CacheManager cacheManager,
        FillRuleItemMapper fillRuleItemMapper
    ) {
        super(fillRuleItemRepository, cacheManager, fillRuleItemMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
