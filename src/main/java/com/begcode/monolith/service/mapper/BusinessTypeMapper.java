package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.service.dto.BusinessTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessType} and its DTO {@link BusinessTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessTypeMapper extends EntityMapper<BusinessTypeDTO, BusinessType> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
