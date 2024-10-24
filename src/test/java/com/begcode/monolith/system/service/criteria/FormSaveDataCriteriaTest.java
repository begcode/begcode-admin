package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FormSaveDataCriteriaTest {

    @Test
    void newFormSaveDataCriteriaHasAllFiltersNullTest() {
        var formSaveDataCriteria = new FormSaveDataCriteria();
        assertThat(formSaveDataCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void formSaveDataCriteriaFluentMethodsCreatesFiltersTest() {
        var formSaveDataCriteria = new FormSaveDataCriteria();

        setAllFilters(formSaveDataCriteria);

        assertThat(formSaveDataCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void formSaveDataCriteriaCopyCreatesNullFilterTest() {
        var formSaveDataCriteria = new FormSaveDataCriteria();
        var copy = formSaveDataCriteria.copy();

        assertThat(formSaveDataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(formSaveDataCriteria)
        );
    }

    @Test
    void formSaveDataCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var formSaveDataCriteria = new FormSaveDataCriteria();
        setAllFilters(formSaveDataCriteria);

        var copy = formSaveDataCriteria.copy();

        assertThat(formSaveDataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(formSaveDataCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var formSaveDataCriteria = new FormSaveDataCriteria();

        assertThat(formSaveDataCriteria).hasToString("FormSaveDataCriteria{}");
    }

    private static void setAllFilters(FormSaveDataCriteria formSaveDataCriteria) {
        formSaveDataCriteria.id();
        formSaveDataCriteria.createdBy();
        formSaveDataCriteria.createdDate();
        formSaveDataCriteria.lastModifiedBy();
        formSaveDataCriteria.lastModifiedDate();
        formSaveDataCriteria.formConfigId();
        formSaveDataCriteria.distinct();
    }

    private static Condition<FormSaveDataCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getFormConfigId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FormSaveDataCriteria> copyFiltersAre(
        FormSaveDataCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getFormConfigId(), copy.getFormConfigId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
