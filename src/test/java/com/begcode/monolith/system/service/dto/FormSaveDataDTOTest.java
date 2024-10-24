package com.begcode.monolith.system.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormSaveDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormSaveDataDTO.class);
        FormSaveDataDTO formSaveDataDTO1 = new FormSaveDataDTO();
        formSaveDataDTO1.setId(1L);
        FormSaveDataDTO formSaveDataDTO2 = new FormSaveDataDTO();
        assertThat(formSaveDataDTO1).isNotEqualTo(formSaveDataDTO2);
        formSaveDataDTO2.setId(formSaveDataDTO1.getId());
        assertThat(formSaveDataDTO1).isEqualTo(formSaveDataDTO2);
        formSaveDataDTO2.setId(2L);
        assertThat(formSaveDataDTO1).isNotEqualTo(formSaveDataDTO2);
        formSaveDataDTO1.setId(null);
        assertThat(formSaveDataDTO1).isNotEqualTo(formSaveDataDTO2);
    }
}
