package com.begcode.monolith.settings.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysFillRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysFillRuleDTO.class);
        SysFillRuleDTO sysFillRuleDTO1 = new SysFillRuleDTO();
        sysFillRuleDTO1.setId(1L);
        SysFillRuleDTO sysFillRuleDTO2 = new SysFillRuleDTO();
        assertThat(sysFillRuleDTO1).isNotEqualTo(sysFillRuleDTO2);
        sysFillRuleDTO2.setId(sysFillRuleDTO1.getId());
        assertThat(sysFillRuleDTO1).isEqualTo(sysFillRuleDTO2);
        sysFillRuleDTO2.setId(2L);
        assertThat(sysFillRuleDTO1).isNotEqualTo(sysFillRuleDTO2);
        sysFillRuleDTO1.setId(null);
        assertThat(sysFillRuleDTO1).isNotEqualTo(sysFillRuleDTO2);
    }
}
