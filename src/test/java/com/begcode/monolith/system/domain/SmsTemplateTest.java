package com.begcode.monolith.system.domain;

import static com.begcode.monolith.system.domain.SmsSupplierTestSamples.*;
import static com.begcode.monolith.system.domain.SmsTemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmsTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsTemplate.class);
        SmsTemplate smsTemplate1 = getSmsTemplateSample1();
        SmsTemplate smsTemplate2 = new SmsTemplate();
        assertThat(smsTemplate1).isNotEqualTo(smsTemplate2);

        smsTemplate2.setId(smsTemplate1.getId());
        assertThat(smsTemplate1).isEqualTo(smsTemplate2);

        smsTemplate2 = getSmsTemplateSample2();
        assertThat(smsTemplate1).isNotEqualTo(smsTemplate2);
    }

    @Test
    void supplierTest() throws Exception {
        SmsTemplate smsTemplate = getSmsTemplateRandomSampleGenerator();
        SmsSupplier smsSupplierBack = getSmsSupplierRandomSampleGenerator();

        smsTemplate.setSupplier(smsSupplierBack);
        assertThat(smsTemplate.getSupplier()).isEqualTo(smsSupplierBack);

        smsTemplate.supplier(null);
        assertThat(smsTemplate.getSupplier()).isNull();
    }
}
