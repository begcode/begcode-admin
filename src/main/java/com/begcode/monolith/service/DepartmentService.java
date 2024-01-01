package com.begcode.monolith.service;

import com.begcode.monolith.domain.Department;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.service.base.DepartmentBaseService;
import com.begcode.monolith.service.mapper.DepartmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
public class DepartmentService extends DepartmentBaseService<DepartmentRepository, Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    public DepartmentService(DepartmentRepository departmentRepository, CacheManager cacheManager, DepartmentMapper departmentMapper) {
        super(departmentRepository, cacheManager, departmentMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
