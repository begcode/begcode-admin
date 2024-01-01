package com.begcode.monolith.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorityDTO.class);
        AuthorityDTO authorityDTO1 = new AuthorityDTO();
        authorityDTO1.setId(1L);
        AuthorityDTO authorityDTO2 = new AuthorityDTO();
        assertThat(authorityDTO1).isNotEqualTo(authorityDTO2);
        authorityDTO2.setId(authorityDTO1.getId());
        assertThat(authorityDTO1).isEqualTo(authorityDTO2);
        authorityDTO2.setId(2L);
        assertThat(authorityDTO1).isNotEqualTo(authorityDTO2);
        authorityDTO1.setId(null);
        assertThat(authorityDTO1).isNotEqualTo(authorityDTO2);
    }
}
