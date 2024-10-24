package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CommonFieldDataCriteriaTest {

    @Test
    void newCommonFieldDataCriteriaHasAllFiltersNullTest() {
        var commonFieldDataCriteria = new CommonFieldDataCriteria();
        assertThat(commonFieldDataCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void commonFieldDataCriteriaFluentMethodsCreatesFiltersTest() {
        var commonFieldDataCriteria = new CommonFieldDataCriteria();

        setAllFilters(commonFieldDataCriteria);

        assertThat(commonFieldDataCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void commonFieldDataCriteriaCopyCreatesNullFilterTest() {
        var commonFieldDataCriteria = new CommonFieldDataCriteria();
        var copy = commonFieldDataCriteria.copy();

        assertThat(commonFieldDataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(commonFieldDataCriteria)
        );
    }

    @Test
    void commonFieldDataCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var commonFieldDataCriteria = new CommonFieldDataCriteria();
        setAllFilters(commonFieldDataCriteria);

        var copy = commonFieldDataCriteria.copy();

        assertThat(commonFieldDataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(commonFieldDataCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var commonFieldDataCriteria = new CommonFieldDataCriteria();

        assertThat(commonFieldDataCriteria).hasToString("CommonFieldDataCriteria{}");
    }

    private static void setAllFilters(CommonFieldDataCriteria commonFieldDataCriteria) {
        commonFieldDataCriteria.id();
        commonFieldDataCriteria.name();
        commonFieldDataCriteria.value();
        commonFieldDataCriteria.label();
        commonFieldDataCriteria.valueType();
        commonFieldDataCriteria.remark();
        commonFieldDataCriteria.sortValue();
        commonFieldDataCriteria.disabled();
        commonFieldDataCriteria.ownerEntityName();
        commonFieldDataCriteria.ownerEntityId();
        commonFieldDataCriteria.distinct();
    }

    private static Condition<CommonFieldDataCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getLabel()) &&
                condition.apply(criteria.getValueType()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getSortValue()) &&
                condition.apply(criteria.getDisabled()) &&
                condition.apply(criteria.getOwnerEntityName()) &&
                condition.apply(criteria.getOwnerEntityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CommonFieldDataCriteria> copyFiltersAre(
        CommonFieldDataCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getLabel(), copy.getLabel()) &&
                condition.apply(criteria.getValueType(), copy.getValueType()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getSortValue(), copy.getSortValue()) &&
                condition.apply(criteria.getDisabled(), copy.getDisabled()) &&
                condition.apply(criteria.getOwnerEntityName(), copy.getOwnerEntityName()) &&
                condition.apply(criteria.getOwnerEntityId(), copy.getOwnerEntityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
