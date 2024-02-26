package com.begcode.monolith.taskjob.domain;

import static com.begcode.monolith.taskjob.domain.TaskJobConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class TaskJobConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskJobConfig.class);
        TaskJobConfig taskJobConfig1 = getTaskJobConfigSample1();
        TaskJobConfig taskJobConfig2 = new TaskJobConfig();
        assertThat(taskJobConfig1).isNotEqualTo(taskJobConfig2);

        taskJobConfig2.setId(taskJobConfig1.getId());
        assertThat(taskJobConfig1).isEqualTo(taskJobConfig2);

        taskJobConfig2 = getTaskJobConfigSample2();
        assertThat(taskJobConfig1).isNotEqualTo(taskJobConfig2);
    }
}
