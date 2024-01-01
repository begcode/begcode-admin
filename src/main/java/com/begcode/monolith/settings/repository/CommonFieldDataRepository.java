package com.begcode.monolith.settings.repository;

import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.repository.base.CommonFieldDataBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonFieldDataRepository extends CommonFieldDataBaseRepository<CommonFieldData> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
