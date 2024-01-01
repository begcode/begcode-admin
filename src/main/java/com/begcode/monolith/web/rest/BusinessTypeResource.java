package com.begcode.monolith.web.rest;

import com.begcode.monolith.repository.BusinessTypeRepository;
import com.begcode.monolith.service.BusinessTypeQueryService;
import com.begcode.monolith.service.BusinessTypeService;
import com.begcode.monolith.web.rest.base.BusinessTypeBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.BusinessType}的REST Controller。
 */
@RestController
@RequestMapping("/api/business-types")
@Tag(name = "business-types", description = "业务类型API接口")
public class BusinessTypeResource extends BusinessTypeBaseResource {

    private final Logger log = LoggerFactory.getLogger(BusinessTypeResource.class);

    public BusinessTypeResource(
        BusinessTypeService businessTypeService,
        BusinessTypeRepository businessTypeRepository,
        BusinessTypeQueryService businessTypeQueryService
    ) {
        super(businessTypeService, businessTypeRepository, businessTypeQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
