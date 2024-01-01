package com.begcode.monolith.oss.web.rest;

import com.begcode.monolith.oss.repository.OssConfigRepository;
import com.begcode.monolith.oss.service.OssConfigQueryService;
import com.begcode.monolith.oss.service.OssConfigService;
import com.begcode.monolith.oss.web.rest.base.OssConfigBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.oss.domain.OssConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api/oss-configs")
@Tag(name = "oss-configs", description = "对象存储配置API接口")
public class OssConfigResource extends OssConfigBaseResource {

    private final Logger log = LoggerFactory.getLogger(OssConfigResource.class);

    public OssConfigResource(
        OssConfigService ossConfigService,
        OssConfigRepository ossConfigRepository,
        OssConfigQueryService ossConfigQueryService
    ) {
        super(ossConfigService, ossConfigRepository, ossConfigQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
