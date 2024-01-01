package com.begcode.monolith.system.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SmsTemplateMapperTest {

    private SmsTemplateMapper smsTemplateMapper;

    @BeforeEach
    public void setUp() {
        smsTemplateMapper = new SmsTemplateMapperImpl();
    }
}
