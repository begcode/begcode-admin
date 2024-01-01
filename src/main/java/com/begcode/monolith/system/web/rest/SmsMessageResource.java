package com.begcode.monolith.system.web.rest;

import com.begcode.monolith.system.repository.SmsMessageRepository;
import com.begcode.monolith.system.service.SmsMessageQueryService;
import com.begcode.monolith.system.service.SmsMessageService;
import com.begcode.monolith.system.web.rest.base.SmsMessageBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.system.domain.SmsMessage}的REST Controller。
 */
@RestController
@RequestMapping("/api/sms-messages")
@Tag(name = "sms-messages", description = "短信消息API接口")
public class SmsMessageResource extends SmsMessageBaseResource {

    private final Logger log = LoggerFactory.getLogger(SmsMessageResource.class);

    public SmsMessageResource(
        SmsMessageService smsMessageService,
        SmsMessageRepository smsMessageRepository,
        SmsMessageQueryService smsMessageQueryService
    ) {
        super(smsMessageService, smsMessageRepository, smsMessageQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
