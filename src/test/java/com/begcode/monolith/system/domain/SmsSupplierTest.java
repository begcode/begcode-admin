package com.begcode.monolith.system.domain;

import static com.begcode.monolith.system.domain.SmsSupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmsSupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsSupplier.class);
        SmsSupplier smsSupplier1 = getSmsSupplierSample1();
        SmsSupplier smsSupplier2 = new SmsSupplier();
        assertThat(smsSupplier1).isNotEqualTo(smsSupplier2);

        smsSupplier2.setId(smsSupplier1.getId());
        assertThat(smsSupplier1).isEqualTo(smsSupplier2);

        smsSupplier2 = getSmsSupplierSample2();
        assertThat(smsSupplier1).isNotEqualTo(smsSupplier2);
    }
}
