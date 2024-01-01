package com.begcode.monolith.settings.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonFieldDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonFieldDataDTO.class);
        CommonFieldDataDTO commonFieldDataDTO1 = new CommonFieldDataDTO();
        commonFieldDataDTO1.setId(1L);
        CommonFieldDataDTO commonFieldDataDTO2 = new CommonFieldDataDTO();
        assertThat(commonFieldDataDTO1).isNotEqualTo(commonFieldDataDTO2);
        commonFieldDataDTO2.setId(commonFieldDataDTO1.getId());
        assertThat(commonFieldDataDTO1).isEqualTo(commonFieldDataDTO2);
        commonFieldDataDTO2.setId(2L);
        assertThat(commonFieldDataDTO1).isNotEqualTo(commonFieldDataDTO2);
        commonFieldDataDTO1.setId(null);
        assertThat(commonFieldDataDTO1).isNotEqualTo(commonFieldDataDTO2);
    }
}
