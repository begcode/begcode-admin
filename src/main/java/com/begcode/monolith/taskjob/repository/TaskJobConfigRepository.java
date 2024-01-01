package com.begcode.monolith.taskjob.repository;

import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.repository.base.TaskJobConfigBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskJobConfigRepository extends TaskJobConfigBaseRepository<TaskJobConfig> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
