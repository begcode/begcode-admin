package com.begcode.monolith.web.rest;

import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.UReportFileQueryService;
import com.begcode.monolith.service.UReportFileService;
import com.begcode.monolith.web.rest.base.UReportFileBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.UReportFile}的REST Controller。
 */
@RestController
@RequestMapping("/api/u-report-files")
@Tag(name = "u-report-files", description = "报表存储API接口")
public class UReportFileResource extends UReportFileBaseResource {

    private final Logger log = LoggerFactory.getLogger(UReportFileResource.class);

    public UReportFileResource(
        UReportFileService uReportFileService,
        UReportFileRepository uReportFileRepository,
        UReportFileQueryService uReportFileQueryService
    ) {
        super(uReportFileService, uReportFileRepository, uReportFileQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
