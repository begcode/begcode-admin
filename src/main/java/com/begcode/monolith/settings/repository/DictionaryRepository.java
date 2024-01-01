package com.begcode.monolith.settings.repository;

import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.repository.base.DictionaryBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictionaryRepository extends DictionaryBaseRepository<Dictionary> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
