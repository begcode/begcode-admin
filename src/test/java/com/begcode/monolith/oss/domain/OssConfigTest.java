package com.begcode.monolith.oss.domain;

import static com.begcode.monolith.oss.domain.OssConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class OssConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OssConfig.class);
        OssConfig ossConfig1 = getOssConfigSample1();
        OssConfig ossConfig2 = new OssConfig();
        assertThat(ossConfig1).isNotEqualTo(ossConfig2);

        ossConfig2.setId(ossConfig1.getId());
        assertThat(ossConfig1).isEqualTo(ossConfig2);

        ossConfig2 = getOssConfigSample2();
        assertThat(ossConfig1).isNotEqualTo(ossConfig2);
    }
}
