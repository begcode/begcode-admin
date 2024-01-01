package com.begcode.monolith.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class DepartmentMapperTest {

    private DepartmentMapper departmentMapper;

    @BeforeEach
    public void setUp() {
        departmentMapper = new DepartmentMapperImpl();
    }
}
