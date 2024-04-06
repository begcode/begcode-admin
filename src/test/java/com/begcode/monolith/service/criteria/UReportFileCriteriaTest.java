package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UReportFileCriteriaTest {

    @Test
    void newUReportFileCriteriaHasAllFiltersNullTest() {
        var uReportFileCriteria = new UReportFileCriteria();
        assertThat(uReportFileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void uReportFileCriteriaFluentMethodsCreatesFiltersTest() {
        var uReportFileCriteria = new UReportFileCriteria();

        setAllFilters(uReportFileCriteria);

        assertThat(uReportFileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void uReportFileCriteriaCopyCreatesNullFilterTest() {
        var uReportFileCriteria = new UReportFileCriteria();
        var copy = uReportFileCriteria.copy();

        assertThat(uReportFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(uReportFileCriteria)
        );
    }

    @Test
    void uReportFileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var uReportFileCriteria = new UReportFileCriteria();
        setAllFilters(uReportFileCriteria);

        var copy = uReportFileCriteria.copy();

        assertThat(uReportFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(uReportFileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var uReportFileCriteria = new UReportFileCriteria();

        assertThat(uReportFileCriteria).hasToString("UReportFileCriteria{}");
    }

    private static void setAllFilters(UReportFileCriteria uReportFileCriteria) {
        uReportFileCriteria.id();
        uReportFileCriteria.name();
        uReportFileCriteria.createAt();
        uReportFileCriteria.updateAt();
        uReportFileCriteria.distinct();
    }

    private static Condition<UReportFileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCreateAt()) &&
                condition.apply(criteria.getUpdateAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UReportFileCriteria> copyFiltersAre(UReportFileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCreateAt(), copy.getCreateAt()) &&
                condition.apply(criteria.getUpdateAt(), copy.getUpdateAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
