package com.begcode.monolith.oss.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OssConfigCriteriaTest {

    @Test
    void newOssConfigCriteriaHasAllFiltersNullTest() {
        var ossConfigCriteria = new OssConfigCriteria();
        assertThat(ossConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void ossConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var ossConfigCriteria = new OssConfigCriteria();

        setAllFilters(ossConfigCriteria);

        assertThat(ossConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void ossConfigCriteriaCopyCreatesNullFilterTest() {
        var ossConfigCriteria = new OssConfigCriteria();
        var copy = ossConfigCriteria.copy();

        assertThat(ossConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(ossConfigCriteria)
        );
    }

    @Test
    void ossConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ossConfigCriteria = new OssConfigCriteria();
        setAllFilters(ossConfigCriteria);

        var copy = ossConfigCriteria.copy();

        assertThat(ossConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(ossConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ossConfigCriteria = new OssConfigCriteria();

        assertThat(ossConfigCriteria).hasToString("OssConfigCriteria{}");
    }

    private static void setAllFilters(OssConfigCriteria ossConfigCriteria) {
        ossConfigCriteria.id();
        ossConfigCriteria.provider();
        ossConfigCriteria.platform();
        ossConfigCriteria.enabled();
        ossConfigCriteria.remark();
        ossConfigCriteria.configData();
        ossConfigCriteria.distinct();
    }

    private static Condition<OssConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProvider()) &&
                condition.apply(criteria.getPlatform()) &&
                condition.apply(criteria.getEnabled()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getConfigData()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OssConfigCriteria> copyFiltersAre(OssConfigCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProvider(), copy.getProvider()) &&
                condition.apply(criteria.getPlatform(), copy.getPlatform()) &&
                condition.apply(criteria.getEnabled(), copy.getEnabled()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getConfigData(), copy.getConfigData()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
