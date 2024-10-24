package com.begcode.monolith.system.domain;

import static com.begcode.monolith.system.domain.FormConfigTestSamples.*;
import static com.begcode.monolith.system.domain.FormSaveDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class FormSaveDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormSaveData.class);
        FormSaveData formSaveData1 = getFormSaveDataSample1();
        FormSaveData formSaveData2 = new FormSaveData();
        assertThat(formSaveData1).isNotEqualTo(formSaveData2);

        formSaveData2.setId(formSaveData1.getId());
        assertThat(formSaveData1).isEqualTo(formSaveData2);

        formSaveData2 = getFormSaveDataSample2();
        assertThat(formSaveData1).isNotEqualTo(formSaveData2);
    }

    @Test
    void formConfigTest() {
        FormSaveData formSaveData = getFormSaveDataRandomSampleGenerator();
        FormConfig formConfigBack = getFormConfigRandomSampleGenerator();

        formSaveData.setFormConfig(formConfigBack);
        assertThat(formSaveData.getFormConfig()).isEqualTo(formConfigBack);

        formSaveData.formConfig(null);
        assertThat(formSaveData.getFormConfig()).isNull();
    }
}
