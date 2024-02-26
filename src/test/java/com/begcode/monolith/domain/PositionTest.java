package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.PositionTestSamples.*;
import static com.begcode.monolith.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Position.class);
        Position position1 = getPositionSample1();
        Position position2 = new Position();
        assertThat(position1).isNotEqualTo(position2);

        position2.setId(position1.getId());
        assertThat(position1).isEqualTo(position2);

        position2 = getPositionSample2();
        assertThat(position1).isNotEqualTo(position2);
    }

    @Test
    void usersTest() throws Exception {
        Position position = getPositionRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        // position.addUsers(userBack);
        assertThat(position.getUsers()).containsOnly(userBack);
        assertThat(userBack.getPosition()).isEqualTo(position);

        // position.removeUsers(userBack);
        assertThat(position.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getPosition()).isNull();

        position.users(new ArrayList<>(Set.of(userBack)));
        assertThat(position.getUsers()).containsOnly(userBack);
        assertThat(userBack.getPosition()).isEqualTo(position);

        position.setUsers(new ArrayList<>());
        assertThat(position.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getPosition()).isNull();
    }
}
