package com.begcode.monolith.system.service;

import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.repository.SmsSupplierRepository;
import com.begcode.monolith.system.service.base.SmsSupplierBaseService;
import com.begcode.monolith.system.service.mapper.SmsSupplierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link SmsSupplier}.
 */
@Service
public class SmsSupplierService extends SmsSupplierBaseService<SmsSupplierRepository, SmsSupplier> {

    private final Logger log = LoggerFactory.getLogger(SmsSupplierService.class);

    public SmsSupplierService(SmsSupplierRepository smsSupplierRepository, CacheManager cacheManager, SmsSupplierMapper smsSupplierMapper) {
        super(smsSupplierRepository, cacheManager, smsSupplierMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
