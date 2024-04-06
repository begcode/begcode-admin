package com.begcode.monolith.taskjob.service.mapper;

import static com.begcode.monolith.taskjob.domain.TaskJobConfigAsserts.*;
import static com.begcode.monolith.taskjob.domain.TaskJobConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskJobConfigMapperTest {

    private TaskJobConfigMapper taskJobConfigMapper;

    @BeforeEach
    void setUp() {
        taskJobConfigMapper = new TaskJobConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTaskJobConfigSample1();
        var actual = taskJobConfigMapper.toEntity(taskJobConfigMapper.toDto(expected));
        assertTaskJobConfigAllPropertiesEquals(expected, actual);
    }
}
