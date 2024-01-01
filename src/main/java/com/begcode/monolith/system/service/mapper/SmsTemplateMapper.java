package com.begcode.monolith.system.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.domain.SmsTemplate;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
import com.begcode.monolith.system.service.dto.SmsTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsTemplate} and its DTO {@link SmsTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface SmsTemplateMapper extends EntityMapper<SmsTemplateDTO, SmsTemplate> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "smsSupplierSignName")
    SmsTemplateDTO toDto(SmsTemplate s);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    SmsTemplate toEntity(SmsTemplateDTO smsTemplateDTO);

    @Named("smsSupplierSignName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "signName", source = "signName")
    SmsSupplierDTO toDtoSmsSupplierSignName(SmsSupplier smsSupplier);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
