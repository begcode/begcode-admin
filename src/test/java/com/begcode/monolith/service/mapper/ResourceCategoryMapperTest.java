package com.begcode.monolith.service.mapper;

import static com.begcode.monolith.domain.ResourceCategoryAsserts.*;
import static com.begcode.monolith.domain.ResourceCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceCategoryMapperTest {

    private ResourceCategoryMapper resourceCategoryMapper;

    @BeforeEach
    void setUp() {
        resourceCategoryMapper = new ResourceCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResourceCategorySample1();
        var actual = resourceCategoryMapper.toEntity(resourceCategoryMapper.toDto(expected));
        assertResourceCategoryAllPropertiesEquals(expected, actual);
    }
}
