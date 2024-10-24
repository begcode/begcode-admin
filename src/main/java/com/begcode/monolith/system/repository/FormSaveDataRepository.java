package com.begcode.monolith.system.repository;

import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.repository.base.FormSaveDataBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FormSaveDataRepository extends FormSaveDataBaseRepository<FormSaveData> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
