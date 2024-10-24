package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SystemConfigCriteriaTest {

    @Test
    void newSystemConfigCriteriaHasAllFiltersNullTest() {
        var systemConfigCriteria = new SystemConfigCriteria();
        assertThat(systemConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void systemConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var systemConfigCriteria = new SystemConfigCriteria();

        setAllFilters(systemConfigCriteria);

        assertThat(systemConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void systemConfigCriteriaCopyCreatesNullFilterTest() {
        var systemConfigCriteria = new SystemConfigCriteria();
        var copy = systemConfigCriteria.copy();

        assertThat(systemConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(systemConfigCriteria)
        );
    }

    @Test
    void systemConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var systemConfigCriteria = new SystemConfigCriteria();
        setAllFilters(systemConfigCriteria);

        var copy = systemConfigCriteria.copy();

        assertThat(systemConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(systemConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var systemConfigCriteria = new SystemConfigCriteria();

        assertThat(systemConfigCriteria).hasToString("SystemConfigCriteria{}");
    }

    private static void setAllFilters(SystemConfigCriteria systemConfigCriteria) {
        systemConfigCriteria.id();
        systemConfigCriteria.categoryName();
        systemConfigCriteria.categoryKey();
        systemConfigCriteria.disabled();
        systemConfigCriteria.sortValue();
        systemConfigCriteria.builtIn();
        systemConfigCriteria.createdBy();
        systemConfigCriteria.createdDate();
        systemConfigCriteria.lastModifiedBy();
        systemConfigCriteria.lastModifiedDate();
        systemConfigCriteria.itemsId();
        systemConfigCriteria.distinct();
    }

    private static Condition<SystemConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCategoryName()) &&
                condition.apply(criteria.getCategoryKey()) &&
                condition.apply(criteria.getDisabled()) &&
                condition.apply(criteria.getSortValue()) &&
                condition.apply(criteria.getBuiltIn()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getItemsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SystemConfigCriteria> copyFiltersAre(
        SystemConfigCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategoryName(), copy.getCategoryName()) &&
                condition.apply(criteria.getCategoryKey(), copy.getCategoryKey()) &&
                condition.apply(criteria.getDisabled(), copy.getDisabled()) &&
                condition.apply(criteria.getSortValue(), copy.getSortValue()) &&
                condition.apply(criteria.getBuiltIn(), copy.getBuiltIn()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getItemsId(), copy.getItemsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
