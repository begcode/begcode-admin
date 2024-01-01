package com.begcode.monolith.system.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SmsMessageMapperTest {

    private SmsMessageMapper smsMessageMapper;

    @BeforeEach
    public void setUp() {
        smsMessageMapper = new SmsMessageMapperImpl();
    }
}
