package com.begcode.monolith.log.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysLogDTO.class);
        SysLogDTO sysLogDTO1 = new SysLogDTO();
        sysLogDTO1.setId(1L);
        SysLogDTO sysLogDTO2 = new SysLogDTO();
        assertThat(sysLogDTO1).isNotEqualTo(sysLogDTO2);
        sysLogDTO2.setId(sysLogDTO1.getId());
        assertThat(sysLogDTO1).isEqualTo(sysLogDTO2);
        sysLogDTO2.setId(2L);
        assertThat(sysLogDTO1).isNotEqualTo(sysLogDTO2);
        sysLogDTO1.setId(null);
        assertThat(sysLogDTO1).isNotEqualTo(sysLogDTO2);
    }
}
