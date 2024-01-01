package com.begcode.monolith.settings.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FillRuleItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FillRuleItemDTO.class);
        FillRuleItemDTO fillRuleItemDTO1 = new FillRuleItemDTO();
        fillRuleItemDTO1.setId(1L);
        FillRuleItemDTO fillRuleItemDTO2 = new FillRuleItemDTO();
        assertThat(fillRuleItemDTO1).isNotEqualTo(fillRuleItemDTO2);
        fillRuleItemDTO2.setId(fillRuleItemDTO1.getId());
        assertThat(fillRuleItemDTO1).isEqualTo(fillRuleItemDTO2);
        fillRuleItemDTO2.setId(2L);
        assertThat(fillRuleItemDTO1).isNotEqualTo(fillRuleItemDTO2);
        fillRuleItemDTO1.setId(null);
        assertThat(fillRuleItemDTO1).isNotEqualTo(fillRuleItemDTO2);
    }
}
