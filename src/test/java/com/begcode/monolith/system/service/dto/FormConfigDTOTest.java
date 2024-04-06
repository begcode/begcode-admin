package com.begcode.monolith.system.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormConfigDTO.class);
        FormConfigDTO formConfigDTO1 = new FormConfigDTO();
        formConfigDTO1.setId(1L);
        FormConfigDTO formConfigDTO2 = new FormConfigDTO();
        assertThat(formConfigDTO1).isNotEqualTo(formConfigDTO2);
        formConfigDTO2.setId(formConfigDTO1.getId());
        assertThat(formConfigDTO1).isEqualTo(formConfigDTO2);
        formConfigDTO2.setId(2L);
        assertThat(formConfigDTO1).isNotEqualTo(formConfigDTO2);
        formConfigDTO1.setId(null);
        assertThat(formConfigDTO1).isNotEqualTo(formConfigDTO2);
    }
}
