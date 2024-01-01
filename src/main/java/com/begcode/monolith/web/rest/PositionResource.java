package com.begcode.monolith.web.rest;

import com.begcode.monolith.repository.PositionRepository;
import com.begcode.monolith.service.PositionQueryService;
import com.begcode.monolith.service.PositionService;
import com.begcode.monolith.web.rest.base.PositionBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.Position}的REST Controller。
 */
@RestController
@RequestMapping("/api/positions")
@Tag(name = "positions", description = "岗位API接口")
public class PositionResource extends PositionBaseResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);

    public PositionResource(
        PositionService positionService,
        PositionRepository positionRepository,
        PositionQueryService positionQueryService
    ) {
        super(positionService, positionRepository, positionQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
