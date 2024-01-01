package com.begcode.monolith.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewPermissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewPermissionDTO.class);
        ViewPermissionDTO viewPermissionDTO1 = new ViewPermissionDTO();
        viewPermissionDTO1.setId(1L);
        ViewPermissionDTO viewPermissionDTO2 = new ViewPermissionDTO();
        assertThat(viewPermissionDTO1).isNotEqualTo(viewPermissionDTO2);
        viewPermissionDTO2.setId(viewPermissionDTO1.getId());
        assertThat(viewPermissionDTO1).isEqualTo(viewPermissionDTO2);
        viewPermissionDTO2.setId(2L);
        assertThat(viewPermissionDTO1).isNotEqualTo(viewPermissionDTO2);
        viewPermissionDTO1.setId(null);
        assertThat(viewPermissionDTO1).isNotEqualTo(viewPermissionDTO2);
    }
}
