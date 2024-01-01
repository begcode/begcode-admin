package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.ApiPermissionTestSamples.*;
import static com.begcode.monolith.domain.AuthorityTestSamples.*;
import static com.begcode.monolith.domain.AuthorityTestSamples.*;
import static com.begcode.monolith.domain.DepartmentTestSamples.*;
import static com.begcode.monolith.domain.UserTestSamples.*;
import static com.begcode.monolith.domain.ViewPermissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authority.class);
        Authority authority1 = getAuthoritySample1();
        Authority authority2 = new Authority();
        assertThat(authority1).isNotEqualTo(authority2);

        authority2.setId(authority1.getId());
        assertThat(authority1).isEqualTo(authority2);

        authority2 = getAuthoritySample2();
        assertThat(authority1).isNotEqualTo(authority2);
    }

    @Test
    void childrenTest() throws Exception {
        Authority authority = getAuthorityRandomSampleGenerator();
        Authority authorityBack = getAuthorityRandomSampleGenerator();

        // authority.addChildren(authorityBack);
        assertThat(authority.getChildren()).containsOnly(authorityBack);
        assertThat(authorityBack.getParent()).isEqualTo(authority);

        // authority.removeChildren(authorityBack);
        assertThat(authority.getChildren()).doesNotContain(authorityBack);
        assertThat(authorityBack.getParent()).isNull();

        authority.children(new ArrayList<>(Set.of(authorityBack)));
        assertThat(authority.getChildren()).containsOnly(authorityBack);
        assertThat(authorityBack.getParent()).isEqualTo(authority);

        authority.setChildren(new ArrayList<>());
        assertThat(authority.getChildren()).doesNotContain(authorityBack);
        assertThat(authorityBack.getParent()).isNull();
    }

    @Test
    void viewPermissionsTest() throws Exception {
        Authority authority = getAuthorityRandomSampleGenerator();
        ViewPermission viewPermissionBack = getViewPermissionRandomSampleGenerator();

        // authority.addViewPermissions(viewPermissionBack);
        assertThat(authority.getViewPermissions()).containsOnly(viewPermissionBack);

        // authority.removeViewPermissions(viewPermissionBack);
        assertThat(authority.getViewPermissions()).doesNotContain(viewPermissionBack);

        authority.viewPermissions(new ArrayList<>(Set.of(viewPermissionBack)));
        assertThat(authority.getViewPermissions()).containsOnly(viewPermissionBack);

        authority.setViewPermissions(new ArrayList<>());
        assertThat(authority.getViewPermissions()).doesNotContain(viewPermissionBack);
    }

    @Test
    void apiPermissionsTest() throws Exception {
        Authority authority = getAuthorityRandomSampleGenerator();
        ApiPermission apiPermissionBack = getApiPermissionRandomSampleGenerator();

        // authority.addApiPermissions(apiPermissionBack);
        assertThat(authority.getApiPermissions()).containsOnly(apiPermissionBack);

        // authority.removeApiPermissions(apiPermissionBack);
        assertThat(authority.getApiPermissions()).doesNotContain(apiPermissionBack);

        authority.apiPermissions(new ArrayList<>(Set.of(apiPermissionBack)));
        assertThat(authority.getApiPermissions()).containsOnly(apiPermissionBack);

        authority.setApiPermissions(new ArrayList<>());
        assertThat(authority.getApiPermissions()).doesNotContain(apiPermissionBack);
    }

    @Test
    void parentTest() throws Exception {
        Authority authority = getAuthorityRandomSampleGenerator();
        Authority authorityBack = getAuthorityRandomSampleGenerator();

        authority.setParent(authorityBack);
        assertThat(authority.getParent()).isEqualTo(authorityBack);

        authority.parent(null);
        assertThat(authority.getParent()).isNull();
    }

    @Test
    void usersTest() throws Exception {
        Authority authority = getAuthorityRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        // authority.addUsers(userBack);
        assertThat(authority.getUsers()).containsOnly(userBack);
        assertThat(userBack.getAuthorities()).containsOnly(authority);

        // authority.removeUsers(userBack);
        assertThat(authority.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getAuthorities()).doesNotContain(authority);

        authority.users(new ArrayList<>(Set.of(userBack)));
        assertThat(authority.getUsers()).containsOnly(userBack);
        assertThat(userBack.getAuthorities()).containsOnly(authority);

        authority.setUsers(new ArrayList<>());
        assertThat(authority.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getAuthorities()).doesNotContain(authority);
    }

    @Test
    void departmentTest() throws Exception {
        Authority authority = getAuthorityRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        // authority.addDepartment(departmentBack);
        assertThat(authority.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getAuthorities()).containsOnly(authority);

        // authority.removeDepartment(departmentBack);
        assertThat(authority.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getAuthorities()).doesNotContain(authority);

        authority.departments(new ArrayList<>(Set.of(departmentBack)));
        assertThat(authority.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getAuthorities()).containsOnly(authority);

        authority.setDepartments(new ArrayList<>());
        assertThat(authority.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getAuthorities()).doesNotContain(authority);
    }
}
