package com.begcode.monolith.taskjob.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskJobConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskJobConfigDTO.class);
        TaskJobConfigDTO taskJobConfigDTO1 = new TaskJobConfigDTO();
        taskJobConfigDTO1.setId(1L);
        TaskJobConfigDTO taskJobConfigDTO2 = new TaskJobConfigDTO();
        assertThat(taskJobConfigDTO1).isNotEqualTo(taskJobConfigDTO2);
        taskJobConfigDTO2.setId(taskJobConfigDTO1.getId());
        assertThat(taskJobConfigDTO1).isEqualTo(taskJobConfigDTO2);
        taskJobConfigDTO2.setId(2L);
        assertThat(taskJobConfigDTO1).isNotEqualTo(taskJobConfigDTO2);
        taskJobConfigDTO1.setId(null);
        assertThat(taskJobConfigDTO1).isNotEqualTo(taskJobConfigDTO2);
    }
}
