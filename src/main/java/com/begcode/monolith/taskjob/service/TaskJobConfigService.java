package com.begcode.monolith.taskjob.service;

import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.repository.TaskJobConfigRepository;
import com.begcode.monolith.taskjob.service.base.TaskJobConfigBaseService;
import com.begcode.monolith.taskjob.service.mapper.TaskJobConfigMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link TaskJobConfig}.
 */
@Service
public class TaskJobConfigService extends TaskJobConfigBaseService<TaskJobConfigRepository, TaskJobConfig> {

    private final Logger log = LoggerFactory.getLogger(TaskJobConfigService.class);

    public TaskJobConfigService(
        Scheduler scheduler,
        TaskJobConfigRepository taskJobConfigRepository,
        CacheManager cacheManager,
        TaskJobConfigMapper taskJobConfigMapper
    ) {
        super(scheduler, taskJobConfigRepository, cacheManager, taskJobConfigMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
