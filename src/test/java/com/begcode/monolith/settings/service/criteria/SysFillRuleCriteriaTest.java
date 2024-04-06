package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SysFillRuleCriteriaTest {

    @Test
    void newSysFillRuleCriteriaHasAllFiltersNullTest() {
        var sysFillRuleCriteria = new SysFillRuleCriteria();
        assertThat(sysFillRuleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sysFillRuleCriteriaFluentMethodsCreatesFiltersTest() {
        var sysFillRuleCriteria = new SysFillRuleCriteria();

        setAllFilters(sysFillRuleCriteria);

        assertThat(sysFillRuleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sysFillRuleCriteriaCopyCreatesNullFilterTest() {
        var sysFillRuleCriteria = new SysFillRuleCriteria();
        var copy = sysFillRuleCriteria.copy();

        assertThat(sysFillRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sysFillRuleCriteria)
        );
    }

    @Test
    void sysFillRuleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sysFillRuleCriteria = new SysFillRuleCriteria();
        setAllFilters(sysFillRuleCriteria);

        var copy = sysFillRuleCriteria.copy();

        assertThat(sysFillRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sysFillRuleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sysFillRuleCriteria = new SysFillRuleCriteria();

        assertThat(sysFillRuleCriteria).hasToString("SysFillRuleCriteria{}");
    }

    private static void setAllFilters(SysFillRuleCriteria sysFillRuleCriteria) {
        sysFillRuleCriteria.id();
        sysFillRuleCriteria.name();
        sysFillRuleCriteria.code();
        sysFillRuleCriteria.desc();
        sysFillRuleCriteria.enabled();
        sysFillRuleCriteria.resetFrequency();
        sysFillRuleCriteria.seqValue();
        sysFillRuleCriteria.fillValue();
        sysFillRuleCriteria.implClass();
        sysFillRuleCriteria.params();
        sysFillRuleCriteria.resetStartTime();
        sysFillRuleCriteria.resetEndTime();
        sysFillRuleCriteria.resetTime();
        sysFillRuleCriteria.ruleItemsId();
        sysFillRuleCriteria.distinct();
    }

    private static Condition<SysFillRuleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDesc()) &&
                condition.apply(criteria.getEnabled()) &&
                condition.apply(criteria.getResetFrequency()) &&
                condition.apply(criteria.getSeqValue()) &&
                condition.apply(criteria.getFillValue()) &&
                condition.apply(criteria.getImplClass()) &&
                condition.apply(criteria.getParams()) &&
                condition.apply(criteria.getResetStartTime()) &&
                condition.apply(criteria.getResetEndTime()) &&
                condition.apply(criteria.getResetTime()) &&
                condition.apply(criteria.getRuleItemsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SysFillRuleCriteria> copyFiltersAre(SysFillRuleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDesc(), copy.getDesc()) &&
                condition.apply(criteria.getEnabled(), copy.getEnabled()) &&
                condition.apply(criteria.getResetFrequency(), copy.getResetFrequency()) &&
                condition.apply(criteria.getSeqValue(), copy.getSeqValue()) &&
                condition.apply(criteria.getFillValue(), copy.getFillValue()) &&
                condition.apply(criteria.getImplClass(), copy.getImplClass()) &&
                condition.apply(criteria.getParams(), copy.getParams()) &&
                condition.apply(criteria.getResetStartTime(), copy.getResetStartTime()) &&
                condition.apply(criteria.getResetEndTime(), copy.getResetEndTime()) &&
                condition.apply(criteria.getResetTime(), copy.getResetTime()) &&
                condition.apply(criteria.getRuleItemsId(), copy.getRuleItemsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
