package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AuthorityCriteriaTest {

    @Test
    void newAuthorityCriteriaHasAllFiltersNullTest() {
        var authorityCriteria = new AuthorityCriteria();
        assertThat(authorityCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void authorityCriteriaFluentMethodsCreatesFiltersTest() {
        var authorityCriteria = new AuthorityCriteria();

        setAllFilters(authorityCriteria);

        assertThat(authorityCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void authorityCriteriaCopyCreatesNullFilterTest() {
        var authorityCriteria = new AuthorityCriteria();
        var copy = authorityCriteria.copy();

        assertThat(authorityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(authorityCriteria)
        );
    }

    @Test
    void authorityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var authorityCriteria = new AuthorityCriteria();
        setAllFilters(authorityCriteria);

        var copy = authorityCriteria.copy();

        assertThat(authorityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(authorityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var authorityCriteria = new AuthorityCriteria();

        assertThat(authorityCriteria).hasToString("AuthorityCriteria{}");
    }

    private static void setAllFilters(AuthorityCriteria authorityCriteria) {
        authorityCriteria.id();
        authorityCriteria.name();
        authorityCriteria.code();
        authorityCriteria.info();
        authorityCriteria.order();
        authorityCriteria.display();
        authorityCriteria.childrenId();
        authorityCriteria.viewPermissionsId();
        authorityCriteria.apiPermissionsId();
        authorityCriteria.parentId();
        authorityCriteria.usersId();
        authorityCriteria.departmentId();
        authorityCriteria.distinct();
    }

    private static Condition<AuthorityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getInfo()) &&
                condition.apply(criteria.getOrder()) &&
                condition.apply(criteria.getDisplay()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getViewPermissionsId()) &&
                condition.apply(criteria.getApiPermissionsId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getUsersId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AuthorityCriteria> copyFiltersAre(AuthorityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getInfo(), copy.getInfo()) &&
                condition.apply(criteria.getOrder(), copy.getOrder()) &&
                condition.apply(criteria.getDisplay(), copy.getDisplay()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getViewPermissionsId(), copy.getViewPermissionsId()) &&
                condition.apply(criteria.getApiPermissionsId(), copy.getApiPermissionsId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getUsersId(), copy.getUsersId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
