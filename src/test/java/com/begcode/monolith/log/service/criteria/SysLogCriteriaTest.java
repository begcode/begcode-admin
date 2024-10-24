package com.begcode.monolith.log.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SysLogCriteriaTest {

    @Test
    void newSysLogCriteriaHasAllFiltersNullTest() {
        var sysLogCriteria = new SysLogCriteria();
        assertThat(sysLogCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sysLogCriteriaFluentMethodsCreatesFiltersTest() {
        var sysLogCriteria = new SysLogCriteria();

        setAllFilters(sysLogCriteria);

        assertThat(sysLogCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sysLogCriteriaCopyCreatesNullFilterTest() {
        var sysLogCriteria = new SysLogCriteria();
        var copy = sysLogCriteria.copy();

        assertThat(sysLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sysLogCriteria)
        );
    }

    @Test
    void sysLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sysLogCriteria = new SysLogCriteria();
        setAllFilters(sysLogCriteria);

        var copy = sysLogCriteria.copy();

        assertThat(sysLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sysLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sysLogCriteria = new SysLogCriteria();

        assertThat(sysLogCriteria).hasToString("SysLogCriteria{}");
    }

    private static void setAllFilters(SysLogCriteria sysLogCriteria) {
        sysLogCriteria.id();
        sysLogCriteria.requestUrl();
        sysLogCriteria.logType();
        sysLogCriteria.logContent();
        sysLogCriteria.operateType();
        sysLogCriteria.userid();
        sysLogCriteria.username();
        sysLogCriteria.ip();
        sysLogCriteria.method();
        sysLogCriteria.requestType();
        sysLogCriteria.costTime();
        sysLogCriteria.createdBy();
        sysLogCriteria.createdDate();
        sysLogCriteria.lastModifiedBy();
        sysLogCriteria.lastModifiedDate();
        sysLogCriteria.distinct();
    }

    private static Condition<SysLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRequestUrl()) &&
                condition.apply(criteria.getLogType()) &&
                condition.apply(criteria.getLogContent()) &&
                condition.apply(criteria.getOperateType()) &&
                condition.apply(criteria.getUserid()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getIp()) &&
                condition.apply(criteria.getMethod()) &&
                condition.apply(criteria.getRequestType()) &&
                condition.apply(criteria.getCostTime()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SysLogCriteria> copyFiltersAre(SysLogCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRequestUrl(), copy.getRequestUrl()) &&
                condition.apply(criteria.getLogType(), copy.getLogType()) &&
                condition.apply(criteria.getLogContent(), copy.getLogContent()) &&
                condition.apply(criteria.getOperateType(), copy.getOperateType()) &&
                condition.apply(criteria.getUserid(), copy.getUserid()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getIp(), copy.getIp()) &&
                condition.apply(criteria.getMethod(), copy.getMethod()) &&
                condition.apply(criteria.getRequestType(), copy.getRequestType()) &&
                condition.apply(criteria.getCostTime(), copy.getCostTime()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
