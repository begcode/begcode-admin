package com.begcode.monolith.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UploadFileCriteriaTest {

    @Test
    void newUploadFileCriteriaHasAllFiltersNullTest() {
        var uploadFileCriteria = new UploadFileCriteria();
        assertThat(uploadFileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void uploadFileCriteriaFluentMethodsCreatesFiltersTest() {
        var uploadFileCriteria = new UploadFileCriteria();

        setAllFilters(uploadFileCriteria);

        assertThat(uploadFileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void uploadFileCriteriaCopyCreatesNullFilterTest() {
        var uploadFileCriteria = new UploadFileCriteria();
        var copy = uploadFileCriteria.copy();

        assertThat(uploadFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(uploadFileCriteria)
        );
    }

    @Test
    void uploadFileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var uploadFileCriteria = new UploadFileCriteria();
        setAllFilters(uploadFileCriteria);

        var copy = uploadFileCriteria.copy();

        assertThat(uploadFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(uploadFileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var uploadFileCriteria = new UploadFileCriteria();

        assertThat(uploadFileCriteria).hasToString("UploadFileCriteria{}");
    }

    private static void setAllFilters(UploadFileCriteria uploadFileCriteria) {
        uploadFileCriteria.id();
        uploadFileCriteria.url();
        uploadFileCriteria.fullName();
        uploadFileCriteria.name();
        uploadFileCriteria.thumb();
        uploadFileCriteria.ext();
        uploadFileCriteria.type();
        uploadFileCriteria.path();
        uploadFileCriteria.folder();
        uploadFileCriteria.ownerEntityName();
        uploadFileCriteria.ownerEntityId();
        uploadFileCriteria.businessTitle();
        uploadFileCriteria.businessDesc();
        uploadFileCriteria.businessStatus();
        uploadFileCriteria.createAt();
        uploadFileCriteria.fileSize();
        uploadFileCriteria.referenceCount();
        uploadFileCriteria.createdBy();
        uploadFileCriteria.createdDate();
        uploadFileCriteria.lastModifiedBy();
        uploadFileCriteria.lastModifiedDate();
        uploadFileCriteria.categoryId();
        uploadFileCriteria.distinct();
    }

    private static Condition<UploadFileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getThumb()) &&
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

    private static Condition<UploadFileCriteria> copyFiltersAre(UploadFileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getThumb(), copy.getThumb()) &&
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
