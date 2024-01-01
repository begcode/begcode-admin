package com.begcode.monolith.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UploadImageMapperTest {

    private UploadImageMapper uploadImageMapper;

    @BeforeEach
    public void setUp() {
        uploadImageMapper = new UploadImageMapperImpl();
    }
}
