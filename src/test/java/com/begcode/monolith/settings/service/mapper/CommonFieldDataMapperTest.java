package com.begcode.monolith.settings.service.mapper;

import static com.begcode.monolith.settings.domain.CommonFieldDataAsserts.*;
import static com.begcode.monolith.settings.domain.CommonFieldDataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommonFieldDataMapperTest {

    private CommonFieldDataMapper commonFieldDataMapper;

    @BeforeEach
    void setUp() {
        commonFieldDataMapper = new CommonFieldDataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommonFieldDataSample1();
        var actual = commonFieldDataMapper.toEntity(commonFieldDataMapper.toDto(expected));
        assertCommonFieldDataAllPropertiesEquals(expected, actual);
    }
}
