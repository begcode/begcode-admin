package com.begcode.monolith.service.mapper;

import static com.begcode.monolith.domain.DepartmentAsserts.*;
import static com.begcode.monolith.domain.DepartmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepartmentMapperTest {

    private DepartmentMapper departmentMapper;

    @BeforeEach
    void setUp() {
        departmentMapper = new DepartmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDepartmentSample1();
        var actual = departmentMapper.toEntity(departmentMapper.toDto(expected));
        assertDepartmentAllPropertiesEquals(expected, actual);
    }
}
