package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FillRuleItemCriteriaTest {

    @Test
    void newFillRuleItemCriteriaHasAllFiltersNullTest() {
        var fillRuleItemCriteria = new FillRuleItemCriteria();
        assertThat(fillRuleItemCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void fillRuleItemCriteriaFluentMethodsCreatesFiltersTest() {
        var fillRuleItemCriteria = new FillRuleItemCriteria();

        setAllFilters(fillRuleItemCriteria);

        assertThat(fillRuleItemCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void fillRuleItemCriteriaCopyCreatesNullFilterTest() {
        var fillRuleItemCriteria = new FillRuleItemCriteria();
        var copy = fillRuleItemCriteria.copy();

        assertThat(fillRuleItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(fillRuleItemCriteria)
        );
    }

    @Test
    void fillRuleItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fillRuleItemCriteria = new FillRuleItemCriteria();
        setAllFilters(fillRuleItemCriteria);

        var copy = fillRuleItemCriteria.copy();

        assertThat(fillRuleItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(fillRuleItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fillRuleItemCriteria = new FillRuleItemCriteria();

        assertThat(fillRuleItemCriteria).hasToString("FillRuleItemCriteria{}");
    }

    private static void setAllFilters(FillRuleItemCriteria fillRuleItemCriteria) {
        fillRuleItemCriteria.id();
        fillRuleItemCriteria.sortValue();
        fillRuleItemCriteria.fieldParamType();
        fillRuleItemCriteria.fieldParamValue();
        fillRuleItemCriteria.datePattern();
        fillRuleItemCriteria.seqLength();
        fillRuleItemCriteria.seqIncrement();
        fillRuleItemCriteria.seqStartValue();
        fillRuleItemCriteria.fillRuleId();
        fillRuleItemCriteria.distinct();
    }

    private static Condition<FillRuleItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSortValue()) &&
                condition.apply(criteria.getFieldParamType()) &&
                condition.apply(criteria.getFieldParamValue()) &&
                condition.apply(criteria.getDatePattern()) &&
                condition.apply(criteria.getSeqLength()) &&
                condition.apply(criteria.getSeqIncrement()) &&
                condition.apply(criteria.getSeqStartValue()) &&
                condition.apply(criteria.getFillRuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FillRuleItemCriteria> copyFiltersAre(
        FillRuleItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSortValue(), copy.getSortValue()) &&
                condition.apply(criteria.getFieldParamType(), copy.getFieldParamType()) &&
                condition.apply(criteria.getFieldParamValue(), copy.getFieldParamValue()) &&
                condition.apply(criteria.getDatePattern(), copy.getDatePattern()) &&
                condition.apply(criteria.getSeqLength(), copy.getSeqLength()) &&
                condition.apply(criteria.getSeqIncrement(), copy.getSeqIncrement()) &&
                condition.apply(criteria.getSeqStartValue(), copy.getSeqStartValue()) &&
                condition.apply(criteria.getFillRuleId(), copy.getFillRuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
