package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.AuthorityTestSamples.*;
import static com.begcode.monolith.domain.DepartmentTestSamples.*;
import static com.begcode.monolith.domain.PositionTestSamples.*;
import static com.begcode.monolith.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = getUserSample1();
        User user2 = new User();
        assertThat(user1).isNotEqualTo(user2);

        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);

        user2 = getUserSample2();
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void departmentTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        user.setDepartment(departmentBack);
        assertThat(user.getDepartment()).isEqualTo(departmentBack);

        user.department(null);
        assertThat(user.getDepartment()).isNull();
    }

    @Test
    void positionTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        user.setPosition(positionBack);
        assertThat(user.getPosition()).isEqualTo(positionBack);

        user.position(null);
        assertThat(user.getPosition()).isNull();
    }

    @Test
    void authoritiesTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        Authority authorityBack = getAuthorityRandomSampleGenerator();

        // user.addAuthorities(authorityBack);
        // assertThat(user.getAuthorities()).containsOnly(authorityBack);

        // user.removeAuthorities(authorityBack);
        // assertThat(user.getAuthorities()).doesNotContain(authorityBack);

        user.authorities(new ArrayList<>(Set.of(authorityBack)));
        assertThat(user.getAuthorities()).containsOnly(authorityBack);

        user.setAuthorities(new ArrayList<>());
        assertThat(user.getAuthorities()).doesNotContain(authorityBack);
    }
}
