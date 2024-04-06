package com.begcode.monolith.system.domain;

import static com.begcode.monolith.domain.BusinessTypeTestSamples.*;
import static com.begcode.monolith.system.domain.FormConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class FormConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormConfig.class);
        FormConfig formConfig1 = getFormConfigSample1();
        FormConfig formConfig2 = new FormConfig();
        assertThat(formConfig1).isNotEqualTo(formConfig2);

        formConfig2.setId(formConfig1.getId());
        assertThat(formConfig1).isEqualTo(formConfig2);

        formConfig2 = getFormConfigSample2();
        assertThat(formConfig1).isNotEqualTo(formConfig2);
    }

    @Test
    void businessTypeTest() throws Exception {
        FormConfig formConfig = getFormConfigRandomSampleGenerator();
        BusinessType businessTypeBack = getBusinessTypeRandomSampleGenerator();

        formConfig.setBusinessType(businessTypeBack);
        assertThat(formConfig.getBusinessType()).isEqualTo(businessTypeBack);

        formConfig.businessType(null);
        assertThat(formConfig.getBusinessType()).isNull();
    }
}
