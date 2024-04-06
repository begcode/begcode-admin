package com.begcode.monolith.system.service.mapper;

import static com.begcode.monolith.system.domain.SmsMessageAsserts.*;
import static com.begcode.monolith.system.domain.SmsMessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmsMessageMapperTest {

    private SmsMessageMapper smsMessageMapper;

    @BeforeEach
    void setUp() {
        smsMessageMapper = new SmsMessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSmsMessageSample1();
        var actual = smsMessageMapper.toEntity(smsMessageMapper.toDto(expected));
        assertSmsMessageAllPropertiesEquals(expected, actual);
    }
}
