package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DepartmentCriteriaTest {

    @Test
    void newDepartmentCriteriaHasAllFiltersNullTest() {
        var departmentCriteria = new DepartmentCriteria();
        assertThat(departmentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void departmentCriteriaFluentMethodsCreatesFiltersTest() {
        var departmentCriteria = new DepartmentCriteria();

        setAllFilters(departmentCriteria);

        assertThat(departmentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void departmentCriteriaCopyCreatesNullFilterTest() {
        var departmentCriteria = new DepartmentCriteria();
        var copy = departmentCriteria.copy();

        assertThat(departmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(departmentCriteria)
        );
    }

    @Test
    void departmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var departmentCriteria = new DepartmentCriteria();
        setAllFilters(departmentCriteria);

        var copy = departmentCriteria.copy();

        assertThat(departmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(departmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var departmentCriteria = new DepartmentCriteria();

        assertThat(departmentCriteria).hasToString("DepartmentCriteria{}");
    }

    private static void setAllFilters(DepartmentCriteria departmentCriteria) {
        departmentCriteria.id();
        departmentCriteria.name();
        departmentCriteria.code();
        departmentCriteria.address();
        departmentCriteria.phoneNum();
        departmentCriteria.logo();
        departmentCriteria.contact();
        departmentCriteria.createUserId();
        departmentCriteria.createTime();
        departmentCriteria.childrenId();
        departmentCriteria.authoritiesId();
        departmentCriteria.parentId();
        departmentCriteria.usersId();
        departmentCriteria.distinct();
    }

    private static Condition<DepartmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getPhoneNum()) &&
                condition.apply(criteria.getLogo()) &&
                condition.apply(criteria.getContact()) &&
                condition.apply(criteria.getCreateUserId()) &&
                condition.apply(criteria.getCreateTime()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getAuthoritiesId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getUsersId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DepartmentCriteria> copyFiltersAre(DepartmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getPhoneNum(), copy.getPhoneNum()) &&
                condition.apply(criteria.getLogo(), copy.getLogo()) &&
                condition.apply(criteria.getContact(), copy.getContact()) &&
                condition.apply(criteria.getCreateUserId(), copy.getCreateUserId()) &&
                condition.apply(criteria.getCreateTime(), copy.getCreateTime()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getAuthoritiesId(), copy.getAuthoritiesId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getUsersId(), copy.getUsersId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
