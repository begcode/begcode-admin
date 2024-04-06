package com.begcode.monolith.system.service.mapper;

import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.service.dto.BusinessTypeDTO;
import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.service.dto.FormConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormConfig} and its DTO {@link FormConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormConfigMapper extends EntityMapper<FormConfigDTO, FormConfig> {
    @Mapping(target = "businessType", source = "businessType", qualifiedByName = "businessTypeName")
    FormConfigDTO toDto(FormConfig s);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    FormConfig toEntity(FormConfigDTO formConfigDTO);

    @Named("businessTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BusinessTypeDTO toDtoBusinessTypeName(BusinessType businessType);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
