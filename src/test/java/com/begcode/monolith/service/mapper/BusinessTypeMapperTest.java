package com.begcode.monolith.service.mapper;

import static com.begcode.monolith.domain.BusinessTypeAsserts.*;
import static com.begcode.monolith.domain.BusinessTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessTypeMapperTest {

    private BusinessTypeMapper businessTypeMapper;

    @BeforeEach
    void setUp() {
        businessTypeMapper = new BusinessTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBusinessTypeSample1();
        var actual = businessTypeMapper.toEntity(businessTypeMapper.toDto(expected));
        assertBusinessTypeAllPropertiesEquals(expected, actual);
    }
}
