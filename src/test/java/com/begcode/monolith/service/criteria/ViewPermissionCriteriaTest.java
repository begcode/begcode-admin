package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ViewPermissionCriteriaTest {

    @Test
    void newViewPermissionCriteriaHasAllFiltersNullTest() {
        var viewPermissionCriteria = new ViewPermissionCriteria();
        assertThat(viewPermissionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void viewPermissionCriteriaFluentMethodsCreatesFiltersTest() {
        var viewPermissionCriteria = new ViewPermissionCriteria();

        setAllFilters(viewPermissionCriteria);

        assertThat(viewPermissionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void viewPermissionCriteriaCopyCreatesNullFilterTest() {
        var viewPermissionCriteria = new ViewPermissionCriteria();
        var copy = viewPermissionCriteria.copy();

        assertThat(viewPermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(viewPermissionCriteria)
        );
    }

    @Test
    void viewPermissionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var viewPermissionCriteria = new ViewPermissionCriteria();
        setAllFilters(viewPermissionCriteria);

        var copy = viewPermissionCriteria.copy();

        assertThat(viewPermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(viewPermissionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var viewPermissionCriteria = new ViewPermissionCriteria();

        assertThat(viewPermissionCriteria).hasToString("ViewPermissionCriteria{}");
    }

    private static void setAllFilters(ViewPermissionCriteria viewPermissionCriteria) {
        viewPermissionCriteria.id();
        viewPermissionCriteria.text();
        viewPermissionCriteria.type();
        viewPermissionCriteria.localeKey();
        viewPermissionCriteria.group();
        viewPermissionCriteria.link();
        viewPermissionCriteria.externalLink();
        viewPermissionCriteria.target();
        viewPermissionCriteria.icon();
        viewPermissionCriteria.disabled();
        viewPermissionCriteria.hide();
        viewPermissionCriteria.hideInBreadcrumb();
        viewPermissionCriteria.shortcut();
        viewPermissionCriteria.shortcutRoot();
        viewPermissionCriteria.reuse();
        viewPermissionCriteria.code();
        viewPermissionCriteria.description();
        viewPermissionCriteria.order();
        viewPermissionCriteria.apiPermissionCodes();
        viewPermissionCriteria.componentFile();
        viewPermissionCriteria.redirect();
        viewPermissionCriteria.childrenId();
        viewPermissionCriteria.parentId();
        viewPermissionCriteria.authoritiesId();
        viewPermissionCriteria.distinct();
    }

    private static Condition<ViewPermissionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getText()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getLocaleKey()) &&
                condition.apply(criteria.getGroup()) &&
                condition.apply(criteria.getLink()) &&
                condition.apply(criteria.getExternalLink()) &&
                condition.apply(criteria.getTarget()) &&
                condition.apply(criteria.getIcon()) &&
                condition.apply(criteria.getDisabled()) &&
                condition.apply(criteria.getHide()) &&
                condition.apply(criteria.getHideInBreadcrumb()) &&
                condition.apply(criteria.getShortcut()) &&
                condition.apply(criteria.getShortcutRoot()) &&
                condition.apply(criteria.getReuse()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getOrder()) &&
                condition.apply(criteria.getApiPermissionCodes()) &&
                condition.apply(criteria.getComponentFile()) &&
                condition.apply(criteria.getRedirect()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getAuthoritiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ViewPermissionCriteria> copyFiltersAre(
        ViewPermissionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getText(), copy.getText()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getLocaleKey(), copy.getLocaleKey()) &&
                condition.apply(criteria.getGroup(), copy.getGroup()) &&
                condition.apply(criteria.getLink(), copy.getLink()) &&
                condition.apply(criteria.getExternalLink(), copy.getExternalLink()) &&
                condition.apply(criteria.getTarget(), copy.getTarget()) &&
                condition.apply(criteria.getIcon(), copy.getIcon()) &&
                condition.apply(criteria.getDisabled(), copy.getDisabled()) &&
                condition.apply(criteria.getHide(), copy.getHide()) &&
                condition.apply(criteria.getHideInBreadcrumb(), copy.getHideInBreadcrumb()) &&
                condition.apply(criteria.getShortcut(), copy.getShortcut()) &&
                condition.apply(criteria.getShortcutRoot(), copy.getShortcutRoot()) &&
                condition.apply(criteria.getReuse(), copy.getReuse()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getOrder(), copy.getOrder()) &&
                condition.apply(criteria.getApiPermissionCodes(), copy.getApiPermissionCodes()) &&
                condition.apply(criteria.getComponentFile(), copy.getComponentFile()) &&
                condition.apply(criteria.getRedirect(), copy.getRedirect()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getAuthoritiesId(), copy.getAuthoritiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
