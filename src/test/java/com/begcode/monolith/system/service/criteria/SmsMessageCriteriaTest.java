package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SmsMessageCriteriaTest {

    @Test
    void newSmsMessageCriteriaHasAllFiltersNullTest() {
        var smsMessageCriteria = new SmsMessageCriteria();
        assertThat(smsMessageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void smsMessageCriteriaFluentMethodsCreatesFiltersTest() {
        var smsMessageCriteria = new SmsMessageCriteria();

        setAllFilters(smsMessageCriteria);

        assertThat(smsMessageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void smsMessageCriteriaCopyCreatesNullFilterTest() {
        var smsMessageCriteria = new SmsMessageCriteria();
        var copy = smsMessageCriteria.copy();

        assertThat(smsMessageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(smsMessageCriteria)
        );
    }

    @Test
    void smsMessageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var smsMessageCriteria = new SmsMessageCriteria();
        setAllFilters(smsMessageCriteria);

        var copy = smsMessageCriteria.copy();

        assertThat(smsMessageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(smsMessageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var smsMessageCriteria = new SmsMessageCriteria();

        assertThat(smsMessageCriteria).hasToString("SmsMessageCriteria{}");
    }

    private static void setAllFilters(SmsMessageCriteria smsMessageCriteria) {
        smsMessageCriteria.id();
        smsMessageCriteria.title();
        smsMessageCriteria.sendType();
        smsMessageCriteria.receiver();
        smsMessageCriteria.params();
        smsMessageCriteria.sendTime();
        smsMessageCriteria.sendStatus();
        smsMessageCriteria.retryNum();
        smsMessageCriteria.failResult();
        smsMessageCriteria.remark();
        smsMessageCriteria.createdBy();
        smsMessageCriteria.createdDate();
        smsMessageCriteria.lastModifiedBy();
        smsMessageCriteria.lastModifiedDate();
        smsMessageCriteria.distinct();
    }

    private static Condition<SmsMessageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getSendType()) &&
                condition.apply(criteria.getReceiver()) &&
                condition.apply(criteria.getParams()) &&
                condition.apply(criteria.getSendTime()) &&
                condition.apply(criteria.getSendStatus()) &&
                condition.apply(criteria.getRetryNum()) &&
                condition.apply(criteria.getFailResult()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SmsMessageCriteria> copyFiltersAre(SmsMessageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getSendType(), copy.getSendType()) &&
                condition.apply(criteria.getReceiver(), copy.getReceiver()) &&
                condition.apply(criteria.getParams(), copy.getParams()) &&
                condition.apply(criteria.getSendTime(), copy.getSendTime()) &&
                condition.apply(criteria.getSendStatus(), copy.getSendStatus()) &&
                condition.apply(criteria.getRetryNum(), copy.getRetryNum()) &&
                condition.apply(criteria.getFailResult(), copy.getFailResult()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
