package com.begcode.monolith.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessTypeDTO.class);
        BusinessTypeDTO businessTypeDTO1 = new BusinessTypeDTO();
        businessTypeDTO1.setId(1L);
        BusinessTypeDTO businessTypeDTO2 = new BusinessTypeDTO();
        assertThat(businessTypeDTO1).isNotEqualTo(businessTypeDTO2);
        businessTypeDTO2.setId(businessTypeDTO1.getId());
        assertThat(businessTypeDTO1).isEqualTo(businessTypeDTO2);
        businessTypeDTO2.setId(2L);
        assertThat(businessTypeDTO1).isNotEqualTo(businessTypeDTO2);
        businessTypeDTO1.setId(null);
        assertThat(businessTypeDTO1).isNotEqualTo(businessTypeDTO2);
    }
}
