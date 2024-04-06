package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FormConfigCriteriaTest {

    @Test
    void newFormConfigCriteriaHasAllFiltersNullTest() {
        var formConfigCriteria = new FormConfigCriteria();
        assertThat(formConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void formConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var formConfigCriteria = new FormConfigCriteria();

        setAllFilters(formConfigCriteria);

        assertThat(formConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void formConfigCriteriaCopyCreatesNullFilterTest() {
        var formConfigCriteria = new FormConfigCriteria();
        var copy = formConfigCriteria.copy();

        assertThat(formConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(formConfigCriteria)
        );
    }

    @Test
    void formConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var formConfigCriteria = new FormConfigCriteria();
        setAllFilters(formConfigCriteria);

        var copy = formConfigCriteria.copy();

        assertThat(formConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(formConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var formConfigCriteria = new FormConfigCriteria();

        assertThat(formConfigCriteria).hasToString("FormConfigCriteria{}");
    }

    private static void setAllFilters(FormConfigCriteria formConfigCriteria) {
        formConfigCriteria.id();
        formConfigCriteria.formKey();
        formConfigCriteria.formName();
        formConfigCriteria.createdBy();
        formConfigCriteria.createdDate();
        formConfigCriteria.lastModifiedBy();
        formConfigCriteria.lastModifiedDate();
        formConfigCriteria.businessTypeId();
        formConfigCriteria.distinct();
    }

    private static Condition<FormConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFormKey()) &&
                condition.apply(criteria.getFormName()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getBusinessTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FormConfigCriteria> copyFiltersAre(FormConfigCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFormKey(), copy.getFormKey()) &&
                condition.apply(criteria.getFormName(), copy.getFormName()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getBusinessTypeId(), copy.getBusinessTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
