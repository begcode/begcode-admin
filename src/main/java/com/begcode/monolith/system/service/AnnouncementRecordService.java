package com.begcode.monolith.system.service;

import com.begcode.monolith.system.domain.AnnouncementRecord;
import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.service.base.AnnouncementRecordBaseService;
import com.begcode.monolith.system.service.mapper.AnnouncementRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AnnouncementRecord}.
 */
@Service
public class AnnouncementRecordService extends AnnouncementRecordBaseService<AnnouncementRecordRepository, AnnouncementRecord> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordService.class);

    public AnnouncementRecordService(
        AnnouncementRecordRepository announcementRecordRepository,
        CacheManager cacheManager,
        AnnouncementRecordMapper announcementRecordMapper
    ) {
        super(announcementRecordRepository, cacheManager, announcementRecordMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
