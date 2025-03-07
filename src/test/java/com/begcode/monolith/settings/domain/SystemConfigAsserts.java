package com.begcode.monolith.settings.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemConfigAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSystemConfigAllPropertiesEquals(SystemConfig expected, SystemConfig actual) {
        assertSystemConfigAutoGeneratedPropertiesEquals(expected, actual);
        assertSystemConfigAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSystemConfigAllUpdatablePropertiesEquals(SystemConfig expected, SystemConfig actual) {
        assertSystemConfigUpdatableFieldsEquals(expected, actual);
        assertSystemConfigUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSystemConfigAutoGeneratedPropertiesEquals(SystemConfig expected, SystemConfig actual) {
        assertThat(expected)
            .as("Verify SystemConfig auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSystemConfigUpdatableFieldsEquals(SystemConfig expected, SystemConfig actual) {
        assertThat(expected)
            .as("Verify SystemConfig relevant properties")
            .satisfies(e -> assertThat(e.getCategoryName()).as("check categoryName").isEqualTo(actual.getCategoryName()))
            .satisfies(e -> assertThat(e.getCategoryKey()).as("check categoryKey").isEqualTo(actual.getCategoryKey()))
            .satisfies(e -> assertThat(e.getDisabled()).as("check disabled").isEqualTo(actual.getDisabled()))
            .satisfies(e -> assertThat(e.getSortValue()).as("check sortValue").isEqualTo(actual.getSortValue()))
            .satisfies(e -> assertThat(e.getBuiltIn()).as("check builtIn").isEqualTo(actual.getBuiltIn()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()))
            .satisfies(e -> assertThat(e.getLastModifiedBy()).as("check lastModifiedBy").isEqualTo(actual.getLastModifiedBy()))
            .satisfies(e -> assertThat(e.getLastModifiedDate()).as("check lastModifiedDate").isEqualTo(actual.getLastModifiedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSystemConfigUpdatableRelationshipsEquals(SystemConfig expected, SystemConfig actual) {}
}
