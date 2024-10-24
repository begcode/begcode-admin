package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.AuthorityTestSamples.*;
import static com.begcode.monolith.domain.ViewPermissionTestSamples.*;
import static com.begcode.monolith.domain.ViewPermissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class ViewPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewPermission.class);
        ViewPermission viewPermission1 = getViewPermissionSample1();
        ViewPermission viewPermission2 = new ViewPermission();
        assertThat(viewPermission1).isNotEqualTo(viewPermission2);

        viewPermission2.setId(viewPermission1.getId());
        assertThat(viewPermission1).isEqualTo(viewPermission2);

        viewPermission2 = getViewPermissionSample2();
        assertThat(viewPermission1).isNotEqualTo(viewPermission2);
    }

    @Test
    void childrenTest() {
        ViewPermission viewPermission = getViewPermissionRandomSampleGenerator();
        ViewPermission viewPermissionBack = getViewPermissionRandomSampleGenerator();

        // todo viewPermission.addChildren(viewPermissionBack);
        // assertThat(viewPermission.getChildren()).containsOnly(viewPermissionBack);
        assertThat(viewPermissionBack.getParent()).isEqualTo(viewPermission);

        // viewPermission.removeChildren(viewPermissionBack);
        // assertThat(viewPermission.getChildren()).doesNotContain(viewPermissionBack);
        assertThat(viewPermissionBack.getParent()).isNull();

        viewPermission.children(new ArrayList<>(Set.of(viewPermissionBack)));
        assertThat(viewPermission.getChildren()).containsOnly(viewPermissionBack);
        assertThat(viewPermissionBack.getParent()).isEqualTo(viewPermission);

        viewPermission.setChildren(new ArrayList<>());
        assertThat(viewPermission.getChildren()).doesNotContain(viewPermissionBack);
        assertThat(viewPermissionBack.getParent()).isNull();
    }

    @Test
    void parentTest() {
        ViewPermission viewPermission = getViewPermissionRandomSampleGenerator();
        ViewPermission viewPermissionBack = getViewPermissionRandomSampleGenerator();

        viewPermission.setParent(viewPermissionBack);
        assertThat(viewPermission.getParent()).isEqualTo(viewPermissionBack);

        viewPermission.parent(null);
        assertThat(viewPermission.getParent()).isNull();
    }
}
