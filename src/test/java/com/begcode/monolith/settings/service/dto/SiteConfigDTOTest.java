package com.begcode.monolith.settings.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteConfigDTO.class);
        SiteConfigDTO siteConfigDTO1 = new SiteConfigDTO();
        siteConfigDTO1.setId(1L);
        SiteConfigDTO siteConfigDTO2 = new SiteConfigDTO();
        assertThat(siteConfigDTO1).isNotEqualTo(siteConfigDTO2);
        siteConfigDTO2.setId(siteConfigDTO1.getId());
        assertThat(siteConfigDTO1).isEqualTo(siteConfigDTO2);
        siteConfigDTO2.setId(2L);
        assertThat(siteConfigDTO1).isNotEqualTo(siteConfigDTO2);
        siteConfigDTO1.setId(null);
        assertThat(siteConfigDTO1).isNotEqualTo(siteConfigDTO2);
    }
}
