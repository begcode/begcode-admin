package com.begcode.monolith.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApiPermissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiPermissionDTO.class);
        ApiPermissionDTO apiPermissionDTO1 = new ApiPermissionDTO();
        apiPermissionDTO1.setId(1L);
        ApiPermissionDTO apiPermissionDTO2 = new ApiPermissionDTO();
        assertThat(apiPermissionDTO1).isNotEqualTo(apiPermissionDTO2);
        apiPermissionDTO2.setId(apiPermissionDTO1.getId());
        assertThat(apiPermissionDTO1).isEqualTo(apiPermissionDTO2);
        apiPermissionDTO2.setId(2L);
        assertThat(apiPermissionDTO1).isNotEqualTo(apiPermissionDTO2);
        apiPermissionDTO1.setId(null);
        assertThat(apiPermissionDTO1).isNotEqualTo(apiPermissionDTO2);
    }
}
