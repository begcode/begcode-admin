package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BusinessTypeCriteriaTest {

    @Test
    void newBusinessTypeCriteriaHasAllFiltersNullTest() {
        var businessTypeCriteria = new BusinessTypeCriteria();
        assertThat(businessTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void businessTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var businessTypeCriteria = new BusinessTypeCriteria();

        setAllFilters(businessTypeCriteria);

        assertThat(businessTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void businessTypeCriteriaCopyCreatesNullFilterTest() {
        var businessTypeCriteria = new BusinessTypeCriteria();
        var copy = businessTypeCriteria.copy();

        assertThat(businessTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(businessTypeCriteria)
        );
    }

    @Test
    void businessTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var businessTypeCriteria = new BusinessTypeCriteria();
        setAllFilters(businessTypeCriteria);

        var copy = businessTypeCriteria.copy();

        assertThat(businessTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(businessTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var businessTypeCriteria = new BusinessTypeCriteria();

        assertThat(businessTypeCriteria).hasToString("BusinessTypeCriteria{}");
    }

    private static void setAllFilters(BusinessTypeCriteria businessTypeCriteria) {
        businessTypeCriteria.id();
        businessTypeCriteria.name();
        businessTypeCriteria.code();
        businessTypeCriteria.description();
        businessTypeCriteria.icon();
        businessTypeCriteria.distinct();
    }

    private static Condition<BusinessTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getIcon()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BusinessTypeCriteria> copyFiltersAre(
        BusinessTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getIcon(), copy.getIcon()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
