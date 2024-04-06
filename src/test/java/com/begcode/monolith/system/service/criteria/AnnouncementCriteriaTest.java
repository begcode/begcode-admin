package com.begcode.monolith.system.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AnnouncementCriteriaTest {

    @Test
    void newAnnouncementCriteriaHasAllFiltersNullTest() {
        var announcementCriteria = new AnnouncementCriteria();
        assertThat(announcementCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void announcementCriteriaFluentMethodsCreatesFiltersTest() {
        var announcementCriteria = new AnnouncementCriteria();

        setAllFilters(announcementCriteria);

        assertThat(announcementCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void announcementCriteriaCopyCreatesNullFilterTest() {
        var announcementCriteria = new AnnouncementCriteria();
        var copy = announcementCriteria.copy();

        assertThat(announcementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(announcementCriteria)
        );
    }

    @Test
    void announcementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var announcementCriteria = new AnnouncementCriteria();
        setAllFilters(announcementCriteria);

        var copy = announcementCriteria.copy();

        assertThat(announcementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(announcementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var announcementCriteria = new AnnouncementCriteria();

        assertThat(announcementCriteria).hasToString("AnnouncementCriteria{}");
    }

    private static void setAllFilters(AnnouncementCriteria announcementCriteria) {
        announcementCriteria.id();
        announcementCriteria.title();
        announcementCriteria.startTime();
        announcementCriteria.endTime();
        announcementCriteria.senderId();
        announcementCriteria.priority();
        announcementCriteria.category();
        announcementCriteria.receiverType();
        announcementCriteria.sendStatus();
        announcementCriteria.sendTime();
        announcementCriteria.cancelTime();
        announcementCriteria.businessType();
        announcementCriteria.businessId();
        announcementCriteria.openType();
        announcementCriteria.openPage();
        announcementCriteria.createdBy();
        announcementCriteria.createdDate();
        announcementCriteria.lastModifiedBy();
        announcementCriteria.lastModifiedDate();
        announcementCriteria.distinct();
    }

    private static Condition<AnnouncementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getEndTime()) &&
                condition.apply(criteria.getSenderId()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getReceiverType()) &&
                condition.apply(criteria.getSendStatus()) &&
                condition.apply(criteria.getSendTime()) &&
                condition.apply(criteria.getCancelTime()) &&
                condition.apply(criteria.getBusinessType()) &&
                condition.apply(criteria.getBusinessId()) &&
                condition.apply(criteria.getOpenType()) &&
                condition.apply(criteria.getOpenPage()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AnnouncementCriteria> copyFiltersAre(
        AnnouncementCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getEndTime(), copy.getEndTime()) &&
                condition.apply(criteria.getSenderId(), copy.getSenderId()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getReceiverType(), copy.getReceiverType()) &&
                condition.apply(criteria.getSendStatus(), copy.getSendStatus()) &&
                condition.apply(criteria.getSendTime(), copy.getSendTime()) &&
                condition.apply(criteria.getCancelTime(), copy.getCancelTime()) &&
                condition.apply(criteria.getBusinessType(), copy.getBusinessType()) &&
                condition.apply(criteria.getBusinessId(), copy.getBusinessId()) &&
                condition.apply(criteria.getOpenType(), copy.getOpenType()) &&
                condition.apply(criteria.getOpenPage(), copy.getOpenPage()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
