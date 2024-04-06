package com.begcode.monolith.settings.service.mapper;

import static com.begcode.monolith.settings.domain.FillRuleItemAsserts.*;
import static com.begcode.monolith.settings.domain.FillRuleItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FillRuleItemMapperTest {

    private FillRuleItemMapper fillRuleItemMapper;

    @BeforeEach
    void setUp() {
        fillRuleItemMapper = new FillRuleItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFillRuleItemSample1();
        var actual = fillRuleItemMapper.toEntity(fillRuleItemMapper.toDto(expected));
        assertFillRuleItemAllPropertiesEquals(expected, actual);
    }
}
