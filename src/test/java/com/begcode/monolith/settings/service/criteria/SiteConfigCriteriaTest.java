package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SiteConfigCriteriaTest {

    @Test
    void newSiteConfigCriteriaHasAllFiltersNullTest() {
        var siteConfigCriteria = new SiteConfigCriteria();
        assertThat(siteConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void siteConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var siteConfigCriteria = new SiteConfigCriteria();

        setAllFilters(siteConfigCriteria);

        assertThat(siteConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void siteConfigCriteriaCopyCreatesNullFilterTest() {
        var siteConfigCriteria = new SiteConfigCriteria();
        var copy = siteConfigCriteria.copy();

        assertThat(siteConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(siteConfigCriteria)
        );
    }

    @Test
    void siteConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var siteConfigCriteria = new SiteConfigCriteria();
        setAllFilters(siteConfigCriteria);

        var copy = siteConfigCriteria.copy();

        assertThat(siteConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(siteConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var siteConfigCriteria = new SiteConfigCriteria();

        assertThat(siteConfigCriteria).hasToString("SiteConfigCriteria{}");
    }

    private static void setAllFilters(SiteConfigCriteria siteConfigCriteria) {
        siteConfigCriteria.id();
        siteConfigCriteria.categoryName();
        siteConfigCriteria.categoryKey();
        siteConfigCriteria.disabled();
        siteConfigCriteria.sortValue();
        siteConfigCriteria.builtIn();
        siteConfigCriteria.createdBy();
        siteConfigCriteria.createdDate();
        siteConfigCriteria.lastModifiedBy();
        siteConfigCriteria.lastModifiedDate();
        siteConfigCriteria.itemsId();
        siteConfigCriteria.distinct();
    }

    private static Condition<SiteConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SiteConfigCriteria> copyFiltersAre(SiteConfigCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
