package com.begcode.monolith.taskjob.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TaskJobConfigMapperTest {

    private TaskJobConfigMapper taskJobConfigMapper;

    @BeforeEach
    public void setUp() {
        taskJobConfigMapper = new TaskJobConfigMapperImpl();
    }
}
