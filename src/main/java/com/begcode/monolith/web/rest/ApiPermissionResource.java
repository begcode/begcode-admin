package com.begcode.monolith.web.rest;

import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.service.ApiPermissionQueryService;
import com.begcode.monolith.service.ApiPermissionService;
import com.begcode.monolith.web.rest.base.ApiPermissionBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.ApiPermission}的REST Controller。
 */
@RestController
@RequestMapping("/api/api-permissions")
@Tag(name = "api-permissions", description = "API权限API接口")
public class ApiPermissionResource extends ApiPermissionBaseResource {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionResource.class);

    public ApiPermissionResource(
        ApiPermissionService apiPermissionService,
        ApiPermissionRepository apiPermissionRepository,
        ApiPermissionQueryService apiPermissionQueryService
    ) {
        super(apiPermissionService, apiPermissionRepository, apiPermissionQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
