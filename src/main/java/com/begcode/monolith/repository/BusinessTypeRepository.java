package com.begcode.monolith.repository;

import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.repository.base.BusinessTypeBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessTypeRepository extends BusinessTypeBaseRepository<BusinessType> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
