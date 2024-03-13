package com.begcode.monolith.settings.domain;

import static com.begcode.monolith.settings.domain.RegionCodeTestSamples.*;
import static com.begcode.monolith.settings.domain.RegionCodeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class RegionCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegionCode.class);
        RegionCode regionCode1 = getRegionCodeSample1();
        RegionCode regionCode2 = new RegionCode();
        assertThat(regionCode1).isNotEqualTo(regionCode2);

        regionCode2.setId(regionCode1.getId());
        assertThat(regionCode1).isEqualTo(regionCode2);

        regionCode2 = getRegionCodeSample2();
        assertThat(regionCode1).isNotEqualTo(regionCode2);
    }

    @Test
    void childrenTest() throws Exception {
        RegionCode regionCode = getRegionCodeRandomSampleGenerator();
        RegionCode regionCodeBack = getRegionCodeRandomSampleGenerator();

        // regionCode.addChildren(regionCodeBack);
        // assertThat(regionCode.getChildren()).containsOnly(regionCodeBack);
        assertThat(regionCodeBack.getParent()).isEqualTo(regionCode);

        // regionCode.removeChildren(regionCodeBack);
        // assertThat(regionCode.getChildren()).doesNotContain(regionCodeBack);
        assertThat(regionCodeBack.getParent()).isNull();

        regionCode.children(new ArrayList<>(Set.of(regionCodeBack)));
        assertThat(regionCode.getChildren()).containsOnly(regionCodeBack);
        assertThat(regionCodeBack.getParent()).isEqualTo(regionCode);

        regionCode.setChildren(new ArrayList<>());
        assertThat(regionCode.getChildren()).doesNotContain(regionCodeBack);
        assertThat(regionCodeBack.getParent()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        RegionCode regionCode = getRegionCodeRandomSampleGenerator();
        RegionCode regionCodeBack = getRegionCodeRandomSampleGenerator();

        regionCode.setParent(regionCodeBack);
        assertThat(regionCode.getParent()).isEqualTo(regionCodeBack);

        regionCode.parent(null);
        assertThat(regionCode.getParent()).isNull();
    }
}
