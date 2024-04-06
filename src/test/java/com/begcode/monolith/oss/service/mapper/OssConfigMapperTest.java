package com.begcode.monolith.oss.service.mapper;

import static com.begcode.monolith.oss.domain.OssConfigAsserts.*;
import static com.begcode.monolith.oss.domain.OssConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OssConfigMapperTest {

    private OssConfigMapper ossConfigMapper;

    @BeforeEach
    void setUp() {
        ossConfigMapper = new OssConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOssConfigSample1();
        var actual = ossConfigMapper.toEntity(ossConfigMapper.toDto(expected));
        assertOssConfigAllPropertiesEquals(expected, actual);
    }
}
