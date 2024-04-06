package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UploadImageCriteriaTest {

    @Test
    void newUploadImageCriteriaHasAllFiltersNullTest() {
        var uploadImageCriteria = new UploadImageCriteria();
        assertThat(uploadImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void uploadImageCriteriaFluentMethodsCreatesFiltersTest() {
        var uploadImageCriteria = new UploadImageCriteria();

        setAllFilters(uploadImageCriteria);

        assertThat(uploadImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void uploadImageCriteriaCopyCreatesNullFilterTest() {
        var uploadImageCriteria = new UploadImageCriteria();
        var copy = uploadImageCriteria.copy();

        assertThat(uploadImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(uploadImageCriteria)
        );
    }

    @Test
    void uploadImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var uploadImageCriteria = new UploadImageCriteria();
        setAllFilters(uploadImageCriteria);

        var copy = uploadImageCriteria.copy();

        assertThat(uploadImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(uploadImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var uploadImageCriteria = new UploadImageCriteria();

        assertThat(uploadImageCriteria).hasToString("UploadImageCriteria{}");
    }

    private static void setAllFilters(UploadImageCriteria uploadImageCriteria) {
        uploadImageCriteria.id();
        uploadImageCriteria.url();
        uploadImageCriteria.fullName();
        uploadImageCriteria.name();
        uploadImageCriteria.ext();
        uploadImageCriteria.type();
        uploadImageCriteria.path();
        uploadImageCriteria.folder();
        uploadImageCriteria.ownerEntityName();
        uploadImageCriteria.ownerEntityId();
        uploadImageCriteria.businessTitle();
        uploadImageCriteria.businessDesc();
        uploadImageCriteria.businessStatus();
        uploadImageCriteria.createAt();
        uploadImageCriteria.fileSize();
        uploadImageCriteria.smartUrl();
        uploadImageCriteria.mediumUrl();
        uploadImageCriteria.referenceCount();
        uploadImageCriteria.createdBy();
        uploadImageCriteria.createdDate();
        uploadImageCriteria.lastModifiedBy();
        uploadImageCriteria.lastModifiedDate();
        uploadImageCriteria.categoryId();
        uploadImageCriteria.distinct();
    }

    private static Condition<UploadImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getExt()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPath()) &&
                condition.apply(criteria.getFolder()) &&
                condition.apply(criteria.getOwnerEntityName()) &&
                condition.apply(criteria.getOwnerEntityId()) &&
                condition.apply(criteria.getBusinessTitle()) &&
                condition.apply(criteria.getBusinessDesc()) &&
                condition.apply(criteria.getBusinessStatus()) &&
                condition.apply(criteria.getCreateAt()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getSmartUrl()) &&
                condition.apply(criteria.getMediumUrl()) &&
                condition.apply(criteria.getReferenceCount()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UploadImageCriteria> copyFiltersAre(UploadImageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getExt(), copy.getExt()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPath(), copy.getPath()) &&
                condition.apply(criteria.getFolder(), copy.getFolder()) &&
                condition.apply(criteria.getOwnerEntityName(), copy.getOwnerEntityName()) &&
                condition.apply(criteria.getOwnerEntityId(), copy.getOwnerEntityId()) &&
                condition.apply(criteria.getBusinessTitle(), copy.getBusinessTitle()) &&
                condition.apply(criteria.getBusinessDesc(), copy.getBusinessDesc()) &&
                condition.apply(criteria.getBusinessStatus(), copy.getBusinessStatus()) &&
                condition.apply(criteria.getCreateAt(), copy.getCreateAt()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getSmartUrl(), copy.getSmartUrl()) &&
                condition.apply(criteria.getMediumUrl(), copy.getMediumUrl()) &&
                condition.apply(criteria.getReferenceCount(), copy.getReferenceCount()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
