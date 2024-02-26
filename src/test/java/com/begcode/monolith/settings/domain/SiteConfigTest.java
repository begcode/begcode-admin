package com.begcode.monolith.settings.domain;

import static com.begcode.monolith.settings.domain.CommonFieldDataTestSamples.*;
import static com.begcode.monolith.settings.domain.SiteConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class SiteConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteConfig.class);
        SiteConfig siteConfig1 = getSiteConfigSample1();
        SiteConfig siteConfig2 = new SiteConfig();
        assertThat(siteConfig1).isNotEqualTo(siteConfig2);

        siteConfig2.setId(siteConfig1.getId());
        assertThat(siteConfig1).isEqualTo(siteConfig2);

        siteConfig2 = getSiteConfigSample2();
        assertThat(siteConfig1).isNotEqualTo(siteConfig2);
    }

    @Test
    void itemsTest() throws Exception {
        SiteConfig siteConfig = getSiteConfigRandomSampleGenerator();
        CommonFieldData commonFieldDataBack = getCommonFieldDataRandomSampleGenerator();

        // siteConfig.addItems(commonFieldDataBack);
        assertThat(siteConfig.getItems()).containsOnly(commonFieldDataBack);

        // siteConfig.removeItems(commonFieldDataBack);
        assertThat(siteConfig.getItems()).doesNotContain(commonFieldDataBack);

        siteConfig.items(new ArrayList<>(Set.of(commonFieldDataBack)));
        assertThat(siteConfig.getItems()).containsOnly(commonFieldDataBack);

        siteConfig.setItems(new ArrayList<>());
        assertThat(siteConfig.getItems()).doesNotContain(commonFieldDataBack);
    }
}
