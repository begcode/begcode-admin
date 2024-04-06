package com.begcode.monolith.system.service.mapper;

import static com.begcode.monolith.system.domain.SmsTemplateAsserts.*;
import static com.begcode.monolith.system.domain.SmsTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmsTemplateMapperTest {

    private SmsTemplateMapper smsTemplateMapper;

    @BeforeEach
    void setUp() {
        smsTemplateMapper = new SmsTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSmsTemplateSample1();
        var actual = smsTemplateMapper.toEntity(smsTemplateMapper.toDto(expected));
        assertSmsTemplateAllPropertiesEquals(expected, actual);
    }
}
