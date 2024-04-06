package com.begcode.monolith.settings.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DictionaryCriteriaTest {

    @Test
    void newDictionaryCriteriaHasAllFiltersNullTest() {
        var dictionaryCriteria = new DictionaryCriteria();
        assertThat(dictionaryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void dictionaryCriteriaFluentMethodsCreatesFiltersTest() {
        var dictionaryCriteria = new DictionaryCriteria();

        setAllFilters(dictionaryCriteria);

        assertThat(dictionaryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void dictionaryCriteriaCopyCreatesNullFilterTest() {
        var dictionaryCriteria = new DictionaryCriteria();
        var copy = dictionaryCriteria.copy();

        assertThat(dictionaryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(dictionaryCriteria)
        );
    }

    @Test
    void dictionaryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dictionaryCriteria = new DictionaryCriteria();
        setAllFilters(dictionaryCriteria);

        var copy = dictionaryCriteria.copy();

        assertThat(dictionaryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(dictionaryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dictionaryCriteria = new DictionaryCriteria();

        assertThat(dictionaryCriteria).hasToString("DictionaryCriteria{}");
    }

    private static void setAllFilters(DictionaryCriteria dictionaryCriteria) {
        dictionaryCriteria.id();
        dictionaryCriteria.dictName();
        dictionaryCriteria.dictKey();
        dictionaryCriteria.disabled();
        dictionaryCriteria.sortValue();
        dictionaryCriteria.builtIn();
        dictionaryCriteria.syncEnum();
        dictionaryCriteria.itemsId();
        dictionaryCriteria.distinct();
    }

    private static Condition<DictionaryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDictName()) &&
                condition.apply(criteria.getDictKey()) &&
                condition.apply(criteria.getDisabled()) &&
                condition.apply(criteria.getSortValue()) &&
                condition.apply(criteria.getBuiltIn()) &&
                condition.apply(criteria.getSyncEnum()) &&
                condition.apply(criteria.getItemsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DictionaryCriteria> copyFiltersAre(DictionaryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDictName(), copy.getDictName()) &&
                condition.apply(criteria.getDictKey(), copy.getDictKey()) &&
                condition.apply(criteria.getDisabled(), copy.getDisabled()) &&
                condition.apply(criteria.getSortValue(), copy.getSortValue()) &&
                condition.apply(criteria.getBuiltIn(), copy.getBuiltIn()) &&
                condition.apply(criteria.getSyncEnum(), copy.getSyncEnum()) &&
                condition.apply(criteria.getItemsId(), copy.getItemsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
