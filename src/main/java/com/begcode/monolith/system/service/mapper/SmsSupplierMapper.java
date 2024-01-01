package com.begcode.monolith.system.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsSupplier} and its DTO {@link SmsSupplierDTO}.
 */
@Mapper(componentModel = "spring")
public interface SmsSupplierMapper extends EntityMapper<SmsSupplierDTO, SmsSupplier> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
