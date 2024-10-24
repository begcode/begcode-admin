package com.begcode.monolith.system.service.mapper;

import static com.begcode.monolith.system.domain.FormSaveDataAsserts.*;
import static com.begcode.monolith.system.domain.FormSaveDataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormSaveDataMapperTest {

    private FormSaveDataMapper formSaveDataMapper;

    @BeforeEach
    void setUp() {
        formSaveDataMapper = new FormSaveDataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFormSaveDataSample1();
        var actual = formSaveDataMapper.toEntity(formSaveDataMapper.toDto(expected));
        assertFormSaveDataAllPropertiesEquals(expected, actual);
    }
}
