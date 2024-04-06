package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ResourceCategoryCriteriaTest {

    @Test
    void newResourceCategoryCriteriaHasAllFiltersNullTest() {
        var resourceCategoryCriteria = new ResourceCategoryCriteria();
        assertThat(resourceCategoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void resourceCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var resourceCategoryCriteria = new ResourceCategoryCriteria();

        setAllFilters(resourceCategoryCriteria);

        assertThat(resourceCategoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void resourceCategoryCriteriaCopyCreatesNullFilterTest() {
        var resourceCategoryCriteria = new ResourceCategoryCriteria();
        var copy = resourceCategoryCriteria.copy();

        assertThat(resourceCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(resourceCategoryCriteria)
        );
    }

    @Test
    void resourceCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var resourceCategoryCriteria = new ResourceCategoryCriteria();
        setAllFilters(resourceCategoryCriteria);

        var copy = resourceCategoryCriteria.copy();

        assertThat(resourceCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(resourceCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var resourceCategoryCriteria = new ResourceCategoryCriteria();

        assertThat(resourceCategoryCriteria).hasToString("ResourceCategoryCriteria{}");
    }

    private static void setAllFilters(ResourceCategoryCriteria resourceCategoryCriteria) {
        resourceCategoryCriteria.id();
        resourceCategoryCriteria.title();
        resourceCategoryCriteria.code();
        resourceCategoryCriteria.orderNumber();
        resourceCategoryCriteria.childrenId();
        resourceCategoryCriteria.parentId();
        resourceCategoryCriteria.imagesId();
        resourceCategoryCriteria.filesId();
        resourceCategoryCriteria.distinct();
    }

    private static Condition<ResourceCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getOrderNumber()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getImagesId()) &&
                condition.apply(criteria.getFilesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ResourceCategoryCriteria> copyFiltersAre(
        ResourceCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getOrderNumber(), copy.getOrderNumber()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getImagesId(), copy.getImagesId()) &&
                condition.apply(criteria.getFilesId(), copy.getFilesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
