package com.begcode.monolith.service.mapper;

import static com.begcode.monolith.domain.UploadImageAsserts.*;
import static com.begcode.monolith.domain.UploadImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UploadImageMapperTest {

    private UploadImageMapper uploadImageMapper;

    @BeforeEach
    void setUp() {
        uploadImageMapper = new UploadImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUploadImageSample1();
        var actual = uploadImageMapper.toEntity(uploadImageMapper.toDto(expected));
        assertUploadImageAllPropertiesEquals(expected, actual);
    }
}
