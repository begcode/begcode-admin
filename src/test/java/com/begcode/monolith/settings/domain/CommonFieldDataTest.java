package com.begcode.monolith.settings.domain;

import static com.begcode.monolith.settings.domain.CommonFieldDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class CommonFieldDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonFieldData.class);
        CommonFieldData commonFieldData1 = getCommonFieldDataSample1();
        CommonFieldData commonFieldData2 = new CommonFieldData();
        assertThat(commonFieldData1).isNotEqualTo(commonFieldData2);

        commonFieldData2.setId(commonFieldData1.getId());
        assertThat(commonFieldData1).isEqualTo(commonFieldData2);

        commonFieldData2 = getCommonFieldDataSample2();
        assertThat(commonFieldData1).isNotEqualTo(commonFieldData2);
    }
}
