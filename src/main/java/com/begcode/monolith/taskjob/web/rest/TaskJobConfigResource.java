package com.begcode.monolith.taskjob.web.rest;

import com.begcode.monolith.taskjob.repository.TaskJobConfigRepository;
import com.begcode.monolith.taskjob.service.TaskJobConfigQueryService;
import com.begcode.monolith.taskjob.service.TaskJobConfigService;
import com.begcode.monolith.taskjob.web.rest.base.TaskJobConfigBaseResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.taskjob.domain.TaskJobConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api/task-job-configs")
@Tag(name = "task-job-configs", description = "任务配置API接口")
public class TaskJobConfigResource extends TaskJobConfigBaseResource {

    private final Logger log = LoggerFactory.getLogger(TaskJobConfigResource.class);

    public TaskJobConfigResource(
        TaskJobConfigService taskJobConfigService,
        TaskJobConfigRepository taskJobConfigRepository,
        TaskJobConfigQueryService taskJobConfigQueryService
    ) {
        super(taskJobConfigService, taskJobConfigRepository, taskJobConfigQueryService);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
