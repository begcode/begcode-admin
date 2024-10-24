package com.begcode.monolith.system.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.service.dto.FormConfigDTO;
import com.begcode.monolith.system.service.dto.FormSaveDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormSaveData} and its DTO {@link FormSaveDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormSaveDataMapper extends EntityMapper<FormSaveDataDTO, FormSaveData> {
    @Mapping(target = "formConfig", source = "formConfig", qualifiedByName = "formConfigFormName")
    FormSaveDataDTO toDto(FormSaveData s);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    FormSaveData toEntity(FormSaveDataDTO formSaveDataDTO);

    @Named("formConfigFormName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formName", source = "formName")
    FormConfigDTO toDtoFormConfigFormName(FormConfig formConfig);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
