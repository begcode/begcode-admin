package com.begcode.monolith.settings.service.mapper;

import static com.begcode.monolith.settings.domain.SysFillRuleAsserts.*;
import static com.begcode.monolith.settings.domain.SysFillRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SysFillRuleMapperTest {

    private SysFillRuleMapper sysFillRuleMapper;

    @BeforeEach
    void setUp() {
        sysFillRuleMapper = new SysFillRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSysFillRuleSample1();
        var actual = sysFillRuleMapper.toEntity(sysFillRuleMapper.toDto(expected));
        assertSysFillRuleAllPropertiesEquals(expected, actual);
    }
}
