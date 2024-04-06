package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserCriteriaTest {

    @Test
    void newUserCriteriaHasAllFiltersNullTest() {
        var userCriteria = new UserCriteria();
        assertThat(userCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userCriteriaFluentMethodsCreatesFiltersTest() {
        var userCriteria = new UserCriteria();

        setAllFilters(userCriteria);

        assertThat(userCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userCriteriaCopyCreatesNullFilterTest() {
        var userCriteria = new UserCriteria();
        var copy = userCriteria.copy();

        assertThat(userCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userCriteria)
        );
    }

    @Test
    void userCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userCriteria = new UserCriteria();
        setAllFilters(userCriteria);

        var copy = userCriteria.copy();

        assertThat(userCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userCriteria = new UserCriteria();

        assertThat(userCriteria).hasToString("UserCriteria{}");
    }

    private static void setAllFilters(UserCriteria userCriteria) {
        userCriteria.id();
        userCriteria.login();
        userCriteria.firstName();
        userCriteria.lastName();
        userCriteria.email();
        userCriteria.mobile();
        userCriteria.birthday();
        userCriteria.activated();
        userCriteria.langKey();
        userCriteria.imageUrl();
        userCriteria.createdBy();
        userCriteria.createdDate();
        userCriteria.lastModifiedBy();
        userCriteria.lastModifiedDate();
        userCriteria.departmentId();
        userCriteria.positionId();
        userCriteria.authoritiesId();
        userCriteria.distinct();
    }

    private static Condition<UserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLogin()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getMobile()) &&
                condition.apply(criteria.getBirthday()) &&
                condition.apply(criteria.getActivated()) &&
                condition.apply(criteria.getLangKey()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getAuthoritiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserCriteria> copyFiltersAre(UserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLogin(), copy.getLogin()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getMobile(), copy.getMobile()) &&
                condition.apply(criteria.getBirthday(), copy.getBirthday()) &&
                condition.apply(criteria.getActivated(), copy.getActivated()) &&
                condition.apply(criteria.getLangKey(), copy.getLangKey()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getAuthoritiesId(), copy.getAuthoritiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
