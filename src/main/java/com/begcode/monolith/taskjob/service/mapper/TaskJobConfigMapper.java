package com.begcode.monolith.taskjob.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.service.dto.TaskJobConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskJobConfig} and its DTO {@link TaskJobConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskJobConfigMapper extends EntityMapper<TaskJobConfigDTO, TaskJobConfig> {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    TaskJobConfig toEntity(TaskJobConfigDTO taskJobConfigDTO);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
