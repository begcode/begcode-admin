package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionCriteriaTest {

    @Test
    void newPositionCriteriaHasAllFiltersNullTest() {
        var positionCriteria = new PositionCriteria();
        assertThat(positionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void positionCriteriaFluentMethodsCreatesFiltersTest() {
        var positionCriteria = new PositionCriteria();

        setAllFilters(positionCriteria);

        assertThat(positionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void positionCriteriaCopyCreatesNullFilterTest() {
        var positionCriteria = new PositionCriteria();
        var copy = positionCriteria.copy();

        assertThat(positionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(positionCriteria)
        );
    }

    @Test
    void positionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionCriteria = new PositionCriteria();
        setAllFilters(positionCriteria);

        var copy = positionCriteria.copy();

        assertThat(positionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(positionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionCriteria = new PositionCriteria();

        assertThat(positionCriteria).hasToString("PositionCriteria{}");
    }

    private static void setAllFilters(PositionCriteria positionCriteria) {
        positionCriteria.id();
        positionCriteria.code();
        positionCriteria.name();
        positionCriteria.sortNo();
        positionCriteria.description();
        positionCriteria.usersId();
        positionCriteria.distinct();
    }

    private static Condition<PositionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSortNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getUsersId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionCriteria> copyFiltersAre(PositionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSortNo(), copy.getSortNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getUsersId(), copy.getUsersId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
