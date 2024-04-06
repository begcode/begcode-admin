package com.begcode.monolith.taskjob.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TaskJobConfigCriteriaTest {

    @Test
    void newTaskJobConfigCriteriaHasAllFiltersNullTest() {
        var taskJobConfigCriteria = new TaskJobConfigCriteria();
        assertThat(taskJobConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void taskJobConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var taskJobConfigCriteria = new TaskJobConfigCriteria();

        setAllFilters(taskJobConfigCriteria);

        assertThat(taskJobConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void taskJobConfigCriteriaCopyCreatesNullFilterTest() {
        var taskJobConfigCriteria = new TaskJobConfigCriteria();
        var copy = taskJobConfigCriteria.copy();

        assertThat(taskJobConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(taskJobConfigCriteria)
        );
    }

    @Test
    void taskJobConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var taskJobConfigCriteria = new TaskJobConfigCriteria();
        setAllFilters(taskJobConfigCriteria);

        var copy = taskJobConfigCriteria.copy();

        assertThat(taskJobConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(taskJobConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var taskJobConfigCriteria = new TaskJobConfigCriteria();

        assertThat(taskJobConfigCriteria).hasToString("TaskJobConfigCriteria{}");
    }

    private static void setAllFilters(TaskJobConfigCriteria taskJobConfigCriteria) {
        taskJobConfigCriteria.id();
        taskJobConfigCriteria.name();
        taskJobConfigCriteria.jobClassName();
        taskJobConfigCriteria.cronExpression();
        taskJobConfigCriteria.parameter();
        taskJobConfigCriteria.description();
        taskJobConfigCriteria.jobStatus();
        taskJobConfigCriteria.createdBy();
        taskJobConfigCriteria.createdDate();
        taskJobConfigCriteria.lastModifiedBy();
        taskJobConfigCriteria.lastModifiedDate();
        taskJobConfigCriteria.distinct();
    }

    private static Condition<TaskJobConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getJobClassName()) &&
                condition.apply(criteria.getCronExpression()) &&
                condition.apply(criteria.getParameter()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getJobStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TaskJobConfigCriteria> copyFiltersAre(
        TaskJobConfigCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getJobClassName(), copy.getJobClassName()) &&
                condition.apply(criteria.getCronExpression(), copy.getCronExpression()) &&
                condition.apply(criteria.getParameter(), copy.getParameter()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getJobStatus(), copy.getJobStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
