package com.begcode.monolith.system.service;

import com.begcode.monolith.service.UserQueryService;
import com.begcode.monolith.system.domain.Announcement;
import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.repository.AnnouncementRepository;
import com.begcode.monolith.system.service.base.AnnouncementBaseService;
import com.begcode.monolith.system.service.mapper.AnnouncementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Announcement}.
 */
@Service
public class AnnouncementService extends AnnouncementBaseService<AnnouncementRepository, Announcement> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

    public AnnouncementService(
        UserQueryService userQueryService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRepository announcementRepository,
        CacheManager cacheManager,
        AnnouncementMapper announcementMapper
    ) {
        super(userQueryService, announcementRecordRepository, announcementRepository, cacheManager, announcementMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
