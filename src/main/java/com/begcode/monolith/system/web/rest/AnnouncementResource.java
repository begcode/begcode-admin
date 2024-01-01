package com.begcode.monolith.system.web.rest;

import com.begcode.monolith.system.repository.AnnouncementRepository;
import com.begcode.monolith.system.service.AnnouncementQueryService;
import com.begcode.monolith.system.service.AnnouncementRecordQueryService;
import com.begcode.monolith.system.service.AnnouncementRecordService;
import com.begcode.monolith.system.service.AnnouncementService;
import com.begcode.monolith.system.web.rest.base.AnnouncementBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.system.domain.Announcement}的REST Controller。
 */
@RestController
@RequestMapping("/api/announcements")
@Tag(name = "announcements", description = "系统通告API接口")
public class AnnouncementResource extends AnnouncementBaseResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementResource.class);

    public AnnouncementResource(
        AnnouncementRecordService announcementRecordService,
        AnnouncementRecordQueryService announcementRecordQueryService,
        AnnouncementService announcementService,
        AnnouncementRepository announcementRepository,
        AnnouncementQueryService announcementQueryService
    ) {
        super(
            announcementRecordService,
            announcementRecordQueryService,
            announcementService,
            announcementRepository,
            announcementQueryService
        );
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
