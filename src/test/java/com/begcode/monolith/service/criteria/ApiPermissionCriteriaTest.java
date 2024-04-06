package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ApiPermissionCriteriaTest {

    @Test
    void newApiPermissionCriteriaHasAllFiltersNullTest() {
        var apiPermissionCriteria = new ApiPermissionCriteria();
        assertThat(apiPermissionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void apiPermissionCriteriaFluentMethodsCreatesFiltersTest() {
        var apiPermissionCriteria = new ApiPermissionCriteria();

        setAllFilters(apiPermissionCriteria);

        assertThat(apiPermissionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void apiPermissionCriteriaCopyCreatesNullFilterTest() {
        var apiPermissionCriteria = new ApiPermissionCriteria();
        var copy = apiPermissionCriteria.copy();

        assertThat(apiPermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(apiPermissionCriteria)
        );
    }

    @Test
    void apiPermissionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var apiPermissionCriteria = new ApiPermissionCriteria();
        setAllFilters(apiPermissionCriteria);

        var copy = apiPermissionCriteria.copy();

        assertThat(apiPermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(apiPermissionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var apiPermissionCriteria = new ApiPermissionCriteria();

        assertThat(apiPermissionCriteria).hasToString("ApiPermissionCriteria{}");
    }

    private static void setAllFilters(ApiPermissionCriteria apiPermissionCriteria) {
        apiPermissionCriteria.id();
        apiPermissionCriteria.serviceName();
        apiPermissionCriteria.name();
        apiPermissionCriteria.code();
        apiPermissionCriteria.description();
        apiPermissionCriteria.type();
        apiPermissionCriteria.method();
        apiPermissionCriteria.url();
        apiPermissionCriteria.status();
        apiPermissionCriteria.childrenId();
        apiPermissionCriteria.parentId();
        apiPermissionCriteria.authoritiesId();
        apiPermissionCriteria.distinct();
    }

    private static Condition<ApiPermissionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getServiceName()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getMethod()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getAuthoritiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ApiPermissionCriteria> copyFiltersAre(
        ApiPermissionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getServiceName(), copy.getServiceName()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getMethod(), copy.getMethod()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getAuthoritiesId(), copy.getAuthoritiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
