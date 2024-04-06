package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RegionCodeCriteriaTest {

    @Test
    void newRegionCodeCriteriaHasAllFiltersNullTest() {
        var regionCodeCriteria = new RegionCodeCriteria();
        assertThat(regionCodeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void regionCodeCriteriaFluentMethodsCreatesFiltersTest() {
        var regionCodeCriteria = new RegionCodeCriteria();

        setAllFilters(regionCodeCriteria);

        assertThat(regionCodeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void regionCodeCriteriaCopyCreatesNullFilterTest() {
        var regionCodeCriteria = new RegionCodeCriteria();
        var copy = regionCodeCriteria.copy();

        assertThat(regionCodeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(regionCodeCriteria)
        );
    }

    @Test
    void regionCodeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var regionCodeCriteria = new RegionCodeCriteria();
        setAllFilters(regionCodeCriteria);

        var copy = regionCodeCriteria.copy();

        assertThat(regionCodeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(regionCodeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var regionCodeCriteria = new RegionCodeCriteria();

        assertThat(regionCodeCriteria).hasToString("RegionCodeCriteria{}");
    }

    private static void setAllFilters(RegionCodeCriteria regionCodeCriteria) {
        regionCodeCriteria.id();
        regionCodeCriteria.name();
        regionCodeCriteria.areaCode();
        regionCodeCriteria.cityCode();
        regionCodeCriteria.mergerName();
        regionCodeCriteria.shortName();
        regionCodeCriteria.zipCode();
        regionCodeCriteria.level();
        regionCodeCriteria.lng();
        regionCodeCriteria.lat();
        regionCodeCriteria.childrenId();
        regionCodeCriteria.parentId();
        regionCodeCriteria.distinct();
    }

    private static Condition<RegionCodeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAreaCode()) &&
                condition.apply(criteria.getCityCode()) &&
                condition.apply(criteria.getMergerName()) &&
                condition.apply(criteria.getShortName()) &&
                condition.apply(criteria.getZipCode()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getLng()) &&
                condition.apply(criteria.getLat()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RegionCodeCriteria> copyFiltersAre(RegionCodeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAreaCode(), copy.getAreaCode()) &&
                condition.apply(criteria.getCityCode(), copy.getCityCode()) &&
                condition.apply(criteria.getMergerName(), copy.getMergerName()) &&
                condition.apply(criteria.getShortName(), copy.getShortName()) &&
                condition.apply(criteria.getZipCode(), copy.getZipCode()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getLng(), copy.getLng()) &&
                condition.apply(criteria.getLat(), copy.getLat()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
