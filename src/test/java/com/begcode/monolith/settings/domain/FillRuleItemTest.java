package com.begcode.monolith.settings.domain;

import static com.begcode.monolith.settings.domain.FillRuleItemTestSamples.*;
import static com.begcode.monolith.settings.domain.SysFillRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class FillRuleItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FillRuleItem.class);
        FillRuleItem fillRuleItem1 = getFillRuleItemSample1();
        FillRuleItem fillRuleItem2 = new FillRuleItem();
        assertThat(fillRuleItem1).isNotEqualTo(fillRuleItem2);

        fillRuleItem2.setId(fillRuleItem1.getId());
        assertThat(fillRuleItem1).isEqualTo(fillRuleItem2);

        fillRuleItem2 = getFillRuleItemSample2();
        assertThat(fillRuleItem1).isNotEqualTo(fillRuleItem2);
    }

    @Test
    void fillRuleTest() {
        FillRuleItem fillRuleItem = getFillRuleItemRandomSampleGenerator();
        SysFillRule sysFillRuleBack = getSysFillRuleRandomSampleGenerator();

        fillRuleItem.setFillRule(sysFillRuleBack);
        assertThat(fillRuleItem.getFillRule()).isEqualTo(sysFillRuleBack);

        fillRuleItem.fillRule(null);
        assertThat(fillRuleItem.getFillRule()).isNull();
    }
}
