package com.begcode.monolith.oss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OssConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OssConfigDTO.class);
        OssConfigDTO ossConfigDTO1 = new OssConfigDTO();
        ossConfigDTO1.setId(1L);
        OssConfigDTO ossConfigDTO2 = new OssConfigDTO();
        assertThat(ossConfigDTO1).isNotEqualTo(ossConfigDTO2);
        ossConfigDTO2.setId(ossConfigDTO1.getId());
        assertThat(ossConfigDTO1).isEqualTo(ossConfigDTO2);
        ossConfigDTO2.setId(2L);
        assertThat(ossConfigDTO1).isNotEqualTo(ossConfigDTO2);
        ossConfigDTO1.setId(null);
        assertThat(ossConfigDTO1).isNotEqualTo(ossConfigDTO2);
    }
}
