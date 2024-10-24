package com.begcode.monolith.settings.web.rest;

import com.begcode.monolith.settings.repository.SystemConfigRepository;
import com.begcode.monolith.settings.service.SystemConfigQueryService;
import com.begcode.monolith.settings.service.SystemConfigService;
import com.begcode.monolith.settings.web.rest.base.SystemConfigBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.settings.domain.SystemConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api/system-configs")
@Tag(name = "system-configs", description = "网站配置API接口")
public class SystemConfigResource extends SystemConfigBaseResource {

    private final Logger log = LoggerFactory.getLogger(SystemConfigResource.class);

    public SystemConfigResource(
        SystemConfigService systemConfigService,
        SystemConfigRepository systemConfigRepository,
        SystemConfigQueryService systemConfigQueryService
    ) {
        super(systemConfigService, systemConfigRepository, systemConfigQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
