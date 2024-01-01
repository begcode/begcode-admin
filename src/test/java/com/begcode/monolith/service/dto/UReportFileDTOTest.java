package com.begcode.monolith.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UReportFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UReportFileDTO.class);
        UReportFileDTO uReportFileDTO1 = new UReportFileDTO();
        uReportFileDTO1.setId(1L);
        UReportFileDTO uReportFileDTO2 = new UReportFileDTO();
        assertThat(uReportFileDTO1).isNotEqualTo(uReportFileDTO2);
        uReportFileDTO2.setId(uReportFileDTO1.getId());
        assertThat(uReportFileDTO1).isEqualTo(uReportFileDTO2);
        uReportFileDTO2.setId(2L);
        assertThat(uReportFileDTO1).isNotEqualTo(uReportFileDTO2);
        uReportFileDTO1.setId(null);
        assertThat(uReportFileDTO1).isNotEqualTo(uReportFileDTO2);
    }
}
