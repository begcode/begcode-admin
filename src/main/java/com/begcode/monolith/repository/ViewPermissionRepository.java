package com.begcode.monolith.repository;

import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.repository.base.ViewPermissionBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViewPermissionRepository extends ViewPermissionBaseRepository<ViewPermission> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
