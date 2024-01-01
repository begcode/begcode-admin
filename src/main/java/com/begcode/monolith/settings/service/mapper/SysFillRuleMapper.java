package com.begcode.monolith.settings.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.begcode.monolith.settings.service.dto.SysFillRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysFillRule} and its DTO {@link SysFillRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SysFillRuleMapper extends EntityMapper<SysFillRuleDTO, SysFillRule> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
