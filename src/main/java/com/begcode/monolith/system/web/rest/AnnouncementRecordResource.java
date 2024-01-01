package com.begcode.monolith.system.web.rest;

import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.service.AnnouncementRecordQueryService;
import com.begcode.monolith.system.service.AnnouncementRecordService;
import com.begcode.monolith.system.web.rest.base.AnnouncementRecordBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.system.domain.AnnouncementRecord}的REST Controller。
 */
@RestController
@RequestMapping("/api/announcement-records")
@Tag(name = "announcement-records", description = "通告阅读记录API接口")
public class AnnouncementRecordResource extends AnnouncementRecordBaseResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordResource.class);

    public AnnouncementRecordResource(
        AnnouncementRecordService announcementRecordService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRecordQueryService announcementRecordQueryService
    ) {
        super(announcementRecordService, announcementRecordRepository, announcementRecordQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
