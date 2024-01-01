package com.begcode.monolith.web.rest;

import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.service.ViewPermissionQueryService;
import com.begcode.monolith.service.ViewPermissionService;
import com.begcode.monolith.web.rest.base.ViewPermissionBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.ViewPermission}的REST Controller。
 */
@RestController
@RequestMapping("/api/view-permissions")
@Tag(name = "view-permissions", description = "可视权限API接口")
public class ViewPermissionResource extends ViewPermissionBaseResource {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionResource.class);

    public ViewPermissionResource(
        ViewPermissionService viewPermissionService,
        ViewPermissionRepository viewPermissionRepository,
        ViewPermissionQueryService viewPermissionQueryService
    ) {
        super(viewPermissionService, viewPermissionRepository, viewPermissionQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
