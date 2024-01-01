package com.begcode.monolith.settings.web.rest;

import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.service.RegionCodeQueryService;
import com.begcode.monolith.settings.service.RegionCodeService;
import com.begcode.monolith.settings.web.rest.base.RegionCodeBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.settings.domain.RegionCode}的REST Controller。
 */
@RestController
@RequestMapping("/api/region-codes")
@Tag(name = "region-codes", description = "行政区划码API接口")
public class RegionCodeResource extends RegionCodeBaseResource {

    private final Logger log = LoggerFactory.getLogger(RegionCodeResource.class);

    public RegionCodeResource(
        RegionCodeService regionCodeService,
        RegionCodeRepository regionCodeRepository,
        RegionCodeQueryService regionCodeQueryService
    ) {
        super(regionCodeService, regionCodeRepository, regionCodeQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
