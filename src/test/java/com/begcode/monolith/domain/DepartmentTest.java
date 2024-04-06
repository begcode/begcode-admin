package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.AuthorityTestSamples.*;
import static com.begcode.monolith.domain.DepartmentTestSamples.*;
import static com.begcode.monolith.domain.DepartmentTestSamples.*;
import static com.begcode.monolith.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class DepartmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Department.class);
        Department department1 = getDepartmentSample1();
        Department department2 = new Department();
        assertThat(department1).isNotEqualTo(department2);

        department2.setId(department1.getId());
        assertThat(department1).isEqualTo(department2);

        department2 = getDepartmentSample2();
        assertThat(department1).isNotEqualTo(department2);
    }

    @Test
    void childrenTest() throws Exception {
        Department department = getDepartmentRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        // todo department.addChildren(departmentBack);
        // assertThat(department.getChildren()).containsOnly(departmentBack);
        assertThat(departmentBack.getParent()).isEqualTo(department);

        // department.removeChildren(departmentBack);
        // assertThat(department.getChildren()).doesNotContain(departmentBack);
        assertThat(departmentBack.getParent()).isNull();

        department.children(new ArrayList<>(Set.of(departmentBack)));
        assertThat(department.getChildren()).containsOnly(departmentBack);
        assertThat(departmentBack.getParent()).isEqualTo(department);

        department.setChildren(new ArrayList<>());
        assertThat(department.getChildren()).doesNotContain(departmentBack);
        assertThat(departmentBack.getParent()).isNull();
    }

    @Test
    void authoritiesTest() throws Exception {
        Department department = getDepartmentRandomSampleGenerator();
        Authority authorityBack = getAuthorityRandomSampleGenerator();

        // todo department.addAuthorities(authorityBack);
        // assertThat(department.getAuthorities()).containsOnly(authorityBack);

        // department.removeAuthorities(authorityBack);
        // assertThat(department.getAuthorities()).doesNotContain(authorityBack);

        department.authorities(new ArrayList<>(Set.of(authorityBack)));
        assertThat(department.getAuthorities()).containsOnly(authorityBack);

        department.setAuthorities(new ArrayList<>());
        assertThat(department.getAuthorities()).doesNotContain(authorityBack);
    }

    @Test
    void parentTest() throws Exception {
        Department department = getDepartmentRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        department.setParent(departmentBack);
        assertThat(department.getParent()).isEqualTo(departmentBack);

        department.parent(null);
        assertThat(department.getParent()).isNull();
    }

    @Test
    void usersTest() throws Exception {
        Department department = getDepartmentRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        // todo department.addUsers(userBack);
        // assertThat(department.getUsers()).containsOnly(userBack);
        assertThat(userBack.getDepartment()).isEqualTo(department);

        // department.removeUsers(userBack);
        // assertThat(department.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getDepartment()).isNull();

        department.users(new ArrayList<>(Set.of(userBack)));
        assertThat(department.getUsers()).containsOnly(userBack);
        assertThat(userBack.getDepartment()).isEqualTo(department);

        department.setUsers(new ArrayList<>());
        assertThat(department.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getDepartment()).isNull();
    }
}
