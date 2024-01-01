package com.begcode.monolith.system.web.rest;

import com.begcode.monolith.system.repository.SmsTemplateRepository;
import com.begcode.monolith.system.service.SmsTemplateQueryService;
import com.begcode.monolith.system.service.SmsTemplateService;
import com.begcode.monolith.system.web.rest.base.SmsTemplateBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.system.domain.SmsTemplate}的REST Controller。
 */
@RestController
@RequestMapping("/api/sms-templates")
@Tag(name = "sms-templates", description = "消息模板API接口")
public class SmsTemplateResource extends SmsTemplateBaseResource {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateResource.class);

    public SmsTemplateResource(
        SmsTemplateService smsTemplateService,
        SmsTemplateRepository smsTemplateRepository,
        SmsTemplateQueryService smsTemplateQueryService
    ) {
        super(smsTemplateService, smsTemplateRepository, smsTemplateQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
