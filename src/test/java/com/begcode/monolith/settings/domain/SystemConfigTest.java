package com.begcode.monolith.settings.domain;

import static com.begcode.monolith.settings.domain.CommonFieldDataTestSamples.*;
import static com.begcode.monolith.settings.domain.SystemConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class SystemConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfig.class);
        SystemConfig systemConfig1 = getSystemConfigSample1();
        SystemConfig systemConfig2 = new SystemConfig();
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);

        systemConfig2.setId(systemConfig1.getId());
        assertThat(systemConfig1).isEqualTo(systemConfig2);

        systemConfig2 = getSystemConfigSample2();
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);
    }

    @Test
    void itemsTest() {
        SystemConfig systemConfig = getSystemConfigRandomSampleGenerator();
        CommonFieldData commonFieldDataBack = getCommonFieldDataRandomSampleGenerator();
        // todo systemConfig.addItems(commonFieldDataBack);
        // assertThat(systemConfig.getItems()).containsOnly(commonFieldDataBack);

        // systemConfig.removeItems(commonFieldDataBack);
        // assertThat(systemConfig.getItems()).doesNotContain(commonFieldDataBack);
    }
}
