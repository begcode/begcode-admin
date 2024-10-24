package com.begcode.monolith.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityAllPropertiesEquals(Authority expected, Authority actual) {
        assertAuthorityAutoGeneratedPropertiesEquals(expected, actual);
        assertAuthorityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityAllUpdatablePropertiesEquals(Authority expected, Authority actual) {
        assertAuthorityUpdatableFieldsEquals(expected, actual);
        assertAuthorityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityAutoGeneratedPropertiesEquals(Authority expected, Authority actual) {
        assertThat(expected)
            .as("Verify Authority auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityUpdatableFieldsEquals(Authority expected, Authority actual) {
        assertThat(expected)
            .as("Verify Authority relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getInfo()).as("check info").isEqualTo(actual.getInfo()))
            .satisfies(e -> assertThat(e.getOrder()).as("check order").isEqualTo(actual.getOrder()))
            .satisfies(e -> assertThat(e.getDisplay()).as("check display").isEqualTo(actual.getDisplay()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityUpdatableRelationshipsEquals(Authority expected, Authority actual) {
        assertThat(expected)
            .as("Verify Authority relationships")
            .satisfies(e -> assertThat(e.getViewPermissions()).as("check viewPermissions").isEqualTo(actual.getViewPermissions()))
            .satisfies(e -> assertThat(e.getApiPermissions()).as("check apiPermissions").isEqualTo(actual.getApiPermissions()))
            .satisfies(e -> assertThat(e.getParent()).as("check parent").isEqualTo(actual.getParent()));
    }
}
