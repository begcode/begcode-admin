package com.begcode.monolith.system.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmsSupplierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsSupplierDTO.class);
        SmsSupplierDTO smsSupplierDTO1 = new SmsSupplierDTO();
        smsSupplierDTO1.setId(1L);
        SmsSupplierDTO smsSupplierDTO2 = new SmsSupplierDTO();
        assertThat(smsSupplierDTO1).isNotEqualTo(smsSupplierDTO2);
        smsSupplierDTO2.setId(smsSupplierDTO1.getId());
        assertThat(smsSupplierDTO1).isEqualTo(smsSupplierDTO2);
        smsSupplierDTO2.setId(2L);
        assertThat(smsSupplierDTO1).isNotEqualTo(smsSupplierDTO2);
        smsSupplierDTO1.setId(null);
        assertThat(smsSupplierDTO1).isNotEqualTo(smsSupplierDTO2);
    }
}
