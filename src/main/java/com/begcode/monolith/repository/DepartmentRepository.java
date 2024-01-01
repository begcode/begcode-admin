package com.begcode.monolith.repository;

import com.begcode.monolith.domain.Department;
import com.begcode.monolith.repository.base.DepartmentBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentRepository extends DepartmentBaseRepository<Department> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
