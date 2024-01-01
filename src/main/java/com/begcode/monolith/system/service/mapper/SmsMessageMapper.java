package com.begcode.monolith.system.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.system.domain.SmsMessage;
import com.begcode.monolith.system.service.dto.SmsMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsMessage} and its DTO {@link SmsMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface SmsMessageMapper extends EntityMapper<SmsMessageDTO, SmsMessage> {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    SmsMessage toEntity(SmsMessageDTO smsMessageDTO);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
