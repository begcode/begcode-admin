package com.begcode.monolith.service;

import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.begcode.monolith.domain.UploadFile;
import com.begcode.monolith.repository.UploadFileRepository;
import com.begcode.monolith.service.base.UploadFileBaseService;
import com.begcode.monolith.service.mapper.UploadFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link UploadFile}.
 */
@Service
public class UploadFileService extends UploadFileBaseService<UploadFileRepository, UploadFile> {

    private final Logger log = LoggerFactory.getLogger(UploadFileService.class);

    public UploadFileService(
        FileStorageService fileStorageService,
        UploadFileRepository uploadFileRepository,
        CacheManager cacheManager,
        UploadFileMapper uploadFileMapper
    ) {
        super(fileStorageService, uploadFileRepository, cacheManager, uploadFileMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
