package com.begcode.monolith.settings.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DictionaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DictionaryDTO.class);
        DictionaryDTO dictionaryDTO1 = new DictionaryDTO();
        dictionaryDTO1.setId(1L);
        DictionaryDTO dictionaryDTO2 = new DictionaryDTO();
        assertThat(dictionaryDTO1).isNotEqualTo(dictionaryDTO2);
        dictionaryDTO2.setId(dictionaryDTO1.getId());
        assertThat(dictionaryDTO1).isEqualTo(dictionaryDTO2);
        dictionaryDTO2.setId(2L);
        assertThat(dictionaryDTO1).isNotEqualTo(dictionaryDTO2);
        dictionaryDTO1.setId(null);
        assertThat(dictionaryDTO1).isNotEqualTo(dictionaryDTO2);
    }
}
