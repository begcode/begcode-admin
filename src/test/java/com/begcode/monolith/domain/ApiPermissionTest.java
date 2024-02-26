package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.ApiPermissionTestSamples.*;
import static com.begcode.monolith.domain.ApiPermissionTestSamples.*;
import static com.begcode.monolith.domain.AuthorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class ApiPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiPermission.class);
        ApiPermission apiPermission1 = getApiPermissionSample1();
        ApiPermission apiPermission2 = new ApiPermission();
        assertThat(apiPermission1).isNotEqualTo(apiPermission2);

        apiPermission2.setId(apiPermission1.getId());
        assertThat(apiPermission1).isEqualTo(apiPermission2);

        apiPermission2 = getApiPermissionSample2();
        assertThat(apiPermission1).isNotEqualTo(apiPermission2);
    }

    @Test
    void childrenTest() throws Exception {
        ApiPermission apiPermission = getApiPermissionRandomSampleGenerator();
        ApiPermission apiPermissionBack = getApiPermissionRandomSampleGenerator();

        // apiPermission.addChildren(apiPermissionBack);
        assertThat(apiPermission.getChildren()).containsOnly(apiPermissionBack);
        assertThat(apiPermissionBack.getParent()).isEqualTo(apiPermission);

        // apiPermission.removeChildren(apiPermissionBack);
        assertThat(apiPermission.getChildren()).doesNotContain(apiPermissionBack);
        assertThat(apiPermissionBack.getParent()).isNull();

        apiPermission.children(new ArrayList<>(Set.of(apiPermissionBack)));
        assertThat(apiPermission.getChildren()).containsOnly(apiPermissionBack);
        assertThat(apiPermissionBack.getParent()).isEqualTo(apiPermission);

        apiPermission.setChildren(new ArrayList<>());
        assertThat(apiPermission.getChildren()).doesNotContain(apiPermissionBack);
        assertThat(apiPermissionBack.getParent()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        ApiPermission apiPermission = getApiPermissionRandomSampleGenerator();
        ApiPermission apiPermissionBack = getApiPermissionRandomSampleGenerator();

        apiPermission.setParent(apiPermissionBack);
        assertThat(apiPermission.getParent()).isEqualTo(apiPermissionBack);

        apiPermission.parent(null);
        assertThat(apiPermission.getParent()).isNull();
    }

    @Test
    void authoritiesTest() throws Exception {
        ApiPermission apiPermission = getApiPermissionRandomSampleGenerator();
        Authority authorityBack = getAuthorityRandomSampleGenerator();

        // apiPermission.addAuthorities(authorityBack);
        assertThat(apiPermission.getAuthorities()).containsOnly(authorityBack);
        assertThat(authorityBack.getApiPermissions()).containsOnly(apiPermission);

        // apiPermission.removeAuthorities(authorityBack);
        assertThat(apiPermission.getAuthorities()).doesNotContain(authorityBack);
        assertThat(authorityBack.getApiPermissions()).doesNotContain(apiPermission);

        apiPermission.authorities(new ArrayList<>(Set.of(authorityBack)));
        assertThat(apiPermission.getAuthorities()).containsOnly(authorityBack);
        assertThat(authorityBack.getApiPermissions()).containsOnly(apiPermission);

        apiPermission.setAuthorities(new ArrayList<>());
        assertThat(apiPermission.getAuthorities()).doesNotContain(authorityBack);
        assertThat(authorityBack.getApiPermissions()).doesNotContain(apiPermission);
    }
}
