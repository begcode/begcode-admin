package com.begcode.monolith.oss.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OssConfigMapperTest {

    private OssConfigMapper ossConfigMapper;

    @BeforeEach
    public void setUp() {
        ossConfigMapper = new OssConfigMapperImpl();
    }
}
