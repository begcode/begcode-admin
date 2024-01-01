package com.begcode.monolith.web.rest;

import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.UploadImageQueryService;
import com.begcode.monolith.service.UploadImageService;
import com.begcode.monolith.web.rest.base.UploadImageBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.UploadImage}的REST Controller。
 */
@RestController
@RequestMapping("/api/upload-images")
@Tag(name = "upload-images", description = "上传图片API接口")
public class UploadImageResource extends UploadImageBaseResource {

    private final Logger log = LoggerFactory.getLogger(UploadImageResource.class);

    public UploadImageResource(
        UploadImageService uploadImageService,
        UploadImageRepository uploadImageRepository,
        UploadImageQueryService uploadImageQueryService
    ) {
        super(uploadImageService, uploadImageRepository, uploadImageQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
