package com.begcode.monolith.service.mapper;

import static com.begcode.monolith.domain.ApiPermissionAsserts.*;
import static com.begcode.monolith.domain.ApiPermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiPermissionMapperTest {

    private ApiPermissionMapper apiPermissionMapper;

    @BeforeEach
    void setUp() {
        apiPermissionMapper = new ApiPermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApiPermissionSample1();
        var actual = apiPermissionMapper.toEntity(apiPermissionMapper.toDto(expected));
        assertApiPermissionAllPropertiesEquals(expected, actual);
    }
}
