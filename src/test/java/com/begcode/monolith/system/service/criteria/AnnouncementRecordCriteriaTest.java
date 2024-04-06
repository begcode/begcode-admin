package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AnnouncementRecordCriteriaTest {

    @Test
    void newAnnouncementRecordCriteriaHasAllFiltersNullTest() {
        var announcementRecordCriteria = new AnnouncementRecordCriteria();
        assertThat(announcementRecordCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void announcementRecordCriteriaFluentMethodsCreatesFiltersTest() {
        var announcementRecordCriteria = new AnnouncementRecordCriteria();

        setAllFilters(announcementRecordCriteria);

        assertThat(announcementRecordCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void announcementRecordCriteriaCopyCreatesNullFilterTest() {
        var announcementRecordCriteria = new AnnouncementRecordCriteria();
        var copy = announcementRecordCriteria.copy();

        assertThat(announcementRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(announcementRecordCriteria)
        );
    }

    @Test
    void announcementRecordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var announcementRecordCriteria = new AnnouncementRecordCriteria();
        setAllFilters(announcementRecordCriteria);

        var copy = announcementRecordCriteria.copy();

        assertThat(announcementRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(announcementRecordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var announcementRecordCriteria = new AnnouncementRecordCriteria();

        assertThat(announcementRecordCriteria).hasToString("AnnouncementRecordCriteria{}");
    }

    private static void setAllFilters(AnnouncementRecordCriteria announcementRecordCriteria) {
        announcementRecordCriteria.id();
        announcementRecordCriteria.anntId();
        announcementRecordCriteria.userId();
        announcementRecordCriteria.hasRead();
        announcementRecordCriteria.readTime();
        announcementRecordCriteria.createdBy();
        announcementRecordCriteria.createdDate();
        announcementRecordCriteria.lastModifiedBy();
        announcementRecordCriteria.lastModifiedDate();
        announcementRecordCriteria.distinct();
    }

    private static Condition<AnnouncementRecordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAnntId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getHasRead()) &&
                condition.apply(criteria.getReadTime()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AnnouncementRecordCriteria> copyFiltersAre(
        AnnouncementRecordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAnntId(), copy.getAnntId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getHasRead(), copy.getHasRead()) &&
                condition.apply(criteria.getReadTime(), copy.getReadTime()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
