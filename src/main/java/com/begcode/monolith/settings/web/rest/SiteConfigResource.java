package com.begcode.monolith.settings.web.rest;

import com.begcode.monolith.settings.repository.SiteConfigRepository;
import com.begcode.monolith.settings.service.SiteConfigQueryService;
import com.begcode.monolith.settings.service.SiteConfigService;
import com.begcode.monolith.settings.web.rest.base.SiteConfigBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.settings.domain.SiteConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api/site-configs")
@Tag(name = "site-configs", description = "网站配置API接口")
public class SiteConfigResource extends SiteConfigBaseResource {

    private final Logger log = LoggerFactory.getLogger(SiteConfigResource.class);

    public SiteConfigResource(
        SiteConfigService siteConfigService,
        SiteConfigRepository siteConfigRepository,
        SiteConfigQueryService siteConfigQueryService
    ) {
        super(siteConfigService, siteConfigRepository, siteConfigQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
