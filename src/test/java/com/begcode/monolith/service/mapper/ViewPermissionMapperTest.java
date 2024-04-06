package com.begcode.monolith.service.mapper;

import static com.begcode.monolith.domain.ViewPermissionAsserts.*;
import static com.begcode.monolith.domain.ViewPermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewPermissionMapperTest {

    private ViewPermissionMapper viewPermissionMapper;

    @BeforeEach
    void setUp() {
        viewPermissionMapper = new ViewPermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getViewPermissionSample1();
        var actual = viewPermissionMapper.toEntity(viewPermissionMapper.toDto(expected));
        assertViewPermissionAllPropertiesEquals(expected, actual);
    }
}
