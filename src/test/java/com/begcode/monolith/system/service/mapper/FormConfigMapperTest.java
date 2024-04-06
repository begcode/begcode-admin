package com.begcode.monolith.system.service.mapper;

import static com.begcode.monolith.system.domain.FormConfigAsserts.*;
import static com.begcode.monolith.system.domain.FormConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormConfigMapperTest {

    private FormConfigMapper formConfigMapper;

    @BeforeEach
    void setUp() {
        formConfigMapper = new FormConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFormConfigSample1();
        var actual = formConfigMapper.toEntity(formConfigMapper.toDto(expected));
        assertFormConfigAllPropertiesEquals(expected, actual);
    }
}
