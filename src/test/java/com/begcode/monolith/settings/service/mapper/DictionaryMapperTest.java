package com.begcode.monolith.settings.service.mapper;

import static com.begcode.monolith.settings.domain.DictionaryAsserts.*;
import static com.begcode.monolith.settings.domain.DictionaryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DictionaryMapperTest {

    private DictionaryMapper dictionaryMapper;

    @BeforeEach
    void setUp() {
        dictionaryMapper = new DictionaryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDictionarySample1();
        var actual = dictionaryMapper.toEntity(dictionaryMapper.toDto(expected));
        assertDictionaryAllPropertiesEquals(expected, actual);
    }
}
