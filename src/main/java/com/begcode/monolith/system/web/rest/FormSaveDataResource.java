package com.begcode.monolith.system.web.rest;

import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.FormSaveDataQueryService;
import com.begcode.monolith.system.service.FormSaveDataService;
import com.begcode.monolith.system.web.rest.base.FormSaveDataBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.system.domain.FormSaveData}的REST Controller。
 */
@RestController
@RequestMapping("/api/form-save-data")
@Tag(name = "form-save-data", description = "表单数据API接口")
public class FormSaveDataResource extends FormSaveDataBaseResource {

    private final Logger log = LoggerFactory.getLogger(FormSaveDataResource.class);

    public FormSaveDataResource(
        FormSaveDataService formSaveDataService,
        FormSaveDataRepository formSaveDataRepository,
        FormSaveDataQueryService formSaveDataQueryService
    ) {
        super(formSaveDataService, formSaveDataRepository, formSaveDataQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
