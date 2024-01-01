package com.begcode.monolith.repository;

import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.repository.base.ResourceCategoryBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceCategoryRepository extends ResourceCategoryBaseRepository<ResourceCategory> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
