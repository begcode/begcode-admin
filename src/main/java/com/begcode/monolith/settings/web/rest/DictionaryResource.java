package com.begcode.monolith.settings.web.rest;

import com.begcode.monolith.settings.repository.DictionaryRepository;
import com.begcode.monolith.settings.service.DictionaryQueryService;
import com.begcode.monolith.settings.service.DictionaryService;
import com.begcode.monolith.settings.web.rest.base.DictionaryBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.settings.domain.Dictionary}的REST Controller。
 */
@RestController
@RequestMapping("/api/dictionaries")
@Tag(name = "dictionaries", description = "数据字典API接口")
public class DictionaryResource extends DictionaryBaseResource {

    private final Logger log = LoggerFactory.getLogger(DictionaryResource.class);

    public DictionaryResource(
        DictionaryService dictionaryService,
        DictionaryRepository dictionaryRepository,
        DictionaryQueryService dictionaryQueryService
    ) {
        super(dictionaryService, dictionaryRepository, dictionaryQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
