package com.begcode.monolith.service;

import com.begcode.monolith.domain.UReportFile;
import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.base.UReportFileBaseService;
import com.begcode.monolith.service.mapper.UReportFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link UReportFile}.
 */
@Service
public class UReportFileService extends UReportFileBaseService<UReportFileRepository, UReportFile> {

    private final Logger log = LoggerFactory.getLogger(UReportFileService.class);

    public UReportFileService(UReportFileRepository uReportFileRepository, CacheManager cacheManager, UReportFileMapper uReportFileMapper) {
        super(uReportFileRepository, cacheManager, uReportFileMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
