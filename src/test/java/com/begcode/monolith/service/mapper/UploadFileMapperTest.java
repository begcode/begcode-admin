package com.begcode.monolith.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UploadFileMapperTest {

    private UploadFileMapper uploadFileMapper;

    @BeforeEach
    public void setUp() {
        uploadFileMapper = new UploadFileMapperImpl();
    }
}
