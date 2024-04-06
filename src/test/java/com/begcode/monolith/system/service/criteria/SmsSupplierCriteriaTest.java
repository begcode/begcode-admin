package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SmsSupplierCriteriaTest {

    @Test
    void newSmsSupplierCriteriaHasAllFiltersNullTest() {
        var smsSupplierCriteria = new SmsSupplierCriteria();
        assertThat(smsSupplierCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void smsSupplierCriteriaFluentMethodsCreatesFiltersTest() {
        var smsSupplierCriteria = new SmsSupplierCriteria();

        setAllFilters(smsSupplierCriteria);

        assertThat(smsSupplierCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void smsSupplierCriteriaCopyCreatesNullFilterTest() {
        var smsSupplierCriteria = new SmsSupplierCriteria();
        var copy = smsSupplierCriteria.copy();

        assertThat(smsSupplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(smsSupplierCriteria)
        );
    }

    @Test
    void smsSupplierCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var smsSupplierCriteria = new SmsSupplierCriteria();
        setAllFilters(smsSupplierCriteria);

        var copy = smsSupplierCriteria.copy();

        assertThat(smsSupplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(smsSupplierCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var smsSupplierCriteria = new SmsSupplierCriteria();

        assertThat(smsSupplierCriteria).hasToString("SmsSupplierCriteria{}");
    }

    private static void setAllFilters(SmsSupplierCriteria smsSupplierCriteria) {
        smsSupplierCriteria.id();
        smsSupplierCriteria.provider();
        smsSupplierCriteria.configData();
        smsSupplierCriteria.signName();
        smsSupplierCriteria.remark();
        smsSupplierCriteria.enabled();
        smsSupplierCriteria.distinct();
    }

    private static Condition<SmsSupplierCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProvider()) &&
                condition.apply(criteria.getConfigData()) &&
                condition.apply(criteria.getSignName()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getEnabled()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SmsSupplierCriteria> copyFiltersAre(SmsSupplierCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProvider(), copy.getProvider()) &&
                condition.apply(criteria.getConfigData(), copy.getConfigData()) &&
                condition.apply(criteria.getSignName(), copy.getSignName()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getEnabled(), copy.getEnabled()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
