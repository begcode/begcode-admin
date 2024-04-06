package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SmsTemplateCriteriaTest {

    @Test
    void newSmsTemplateCriteriaHasAllFiltersNullTest() {
        var smsTemplateCriteria = new SmsTemplateCriteria();
        assertThat(smsTemplateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void smsTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var smsTemplateCriteria = new SmsTemplateCriteria();

        setAllFilters(smsTemplateCriteria);

        assertThat(smsTemplateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void smsTemplateCriteriaCopyCreatesNullFilterTest() {
        var smsTemplateCriteria = new SmsTemplateCriteria();
        var copy = smsTemplateCriteria.copy();

        assertThat(smsTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(smsTemplateCriteria)
        );
    }

    @Test
    void smsTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var smsTemplateCriteria = new SmsTemplateCriteria();
        setAllFilters(smsTemplateCriteria);

        var copy = smsTemplateCriteria.copy();

        assertThat(smsTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(smsTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var smsTemplateCriteria = new SmsTemplateCriteria();

        assertThat(smsTemplateCriteria).hasToString("SmsTemplateCriteria{}");
    }

    private static void setAllFilters(SmsTemplateCriteria smsTemplateCriteria) {
        smsTemplateCriteria.id();
        smsTemplateCriteria.name();
        smsTemplateCriteria.code();
        smsTemplateCriteria.sendType();
        smsTemplateCriteria.content();
        smsTemplateCriteria.testJson();
        smsTemplateCriteria.type();
        smsTemplateCriteria.remark();
        smsTemplateCriteria.enabled();
        smsTemplateCriteria.createdBy();
        smsTemplateCriteria.createdDate();
        smsTemplateCriteria.lastModifiedBy();
        smsTemplateCriteria.lastModifiedDate();
        smsTemplateCriteria.supplierId();
        smsTemplateCriteria.distinct();
    }

    private static Condition<SmsTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getSendType()) &&
                condition.apply(criteria.getContent()) &&
                condition.apply(criteria.getTestJson()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getEnabled()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getSupplierId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SmsTemplateCriteria> copyFiltersAre(SmsTemplateCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getSendType(), copy.getSendType()) &&
                condition.apply(criteria.getContent(), copy.getContent()) &&
                condition.apply(criteria.getTestJson(), copy.getTestJson()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getEnabled(), copy.getEnabled()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getSupplierId(), copy.getSupplierId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
