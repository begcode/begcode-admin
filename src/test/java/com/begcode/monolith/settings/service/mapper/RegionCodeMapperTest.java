package com.begcode.monolith.settings.service.mapper;

import static com.begcode.monolith.settings.domain.RegionCodeAsserts.*;
import static com.begcode.monolith.settings.domain.RegionCodeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegionCodeMapperTest {

    private RegionCodeMapper regionCodeMapper;

    @BeforeEach
    void setUp() {
        regionCodeMapper = new RegionCodeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRegionCodeSample1();
        var actual = regionCodeMapper.toEntity(regionCodeMapper.toDto(expected));
        assertRegionCodeAllPropertiesEquals(expected, actual);
    }
}
