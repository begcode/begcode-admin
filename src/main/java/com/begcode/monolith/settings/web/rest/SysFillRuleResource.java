package com.begcode.monolith.settings.web.rest;

import com.begcode.monolith.settings.repository.SysFillRuleRepository;
import com.begcode.monolith.settings.service.SysFillRuleQueryService;
import com.begcode.monolith.settings.service.SysFillRuleService;
import com.begcode.monolith.settings.web.rest.base.SysFillRuleBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.settings.domain.SysFillRule}的REST Controller。
 */
@RestController
@RequestMapping("/api/sys-fill-rules")
@Tag(name = "sys-fill-rules", description = "填充规则API接口")
public class SysFillRuleResource extends SysFillRuleBaseResource {

    private final Logger log = LoggerFactory.getLogger(SysFillRuleResource.class);

    public SysFillRuleResource(
        SysFillRuleService sysFillRuleService,
        SysFillRuleRepository sysFillRuleRepository,
        SysFillRuleQueryService sysFillRuleQueryService
    ) {
        super(sysFillRuleService, sysFillRuleRepository, sysFillRuleQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
