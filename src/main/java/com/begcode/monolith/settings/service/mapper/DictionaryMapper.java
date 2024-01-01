package com.begcode.monolith.settings.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.service.dto.DictionaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dictionary} and its DTO {@link DictionaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DictionaryMapper extends EntityMapper<DictionaryDTO, Dictionary> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
