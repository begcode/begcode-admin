package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.ResourceCategoryTestSamples.*;
import static com.begcode.monolith.domain.ResourceCategoryTestSamples.*;
import static com.begcode.monolith.domain.UploadFileTestSamples.*;
import static com.begcode.monolith.domain.UploadImageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ResourceCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceCategory.class);
        ResourceCategory resourceCategory1 = getResourceCategorySample1();
        ResourceCategory resourceCategory2 = new ResourceCategory();
        assertThat(resourceCategory1).isNotEqualTo(resourceCategory2);

        resourceCategory2.setId(resourceCategory1.getId());
        assertThat(resourceCategory1).isEqualTo(resourceCategory2);

        resourceCategory2 = getResourceCategorySample2();
        assertThat(resourceCategory1).isNotEqualTo(resourceCategory2);
    }

    @Test
    void childrenTest() throws Exception {
        ResourceCategory resourceCategory = getResourceCategoryRandomSampleGenerator();
        ResourceCategory resourceCategoryBack = getResourceCategoryRandomSampleGenerator();

        // resourceCategory.addChildren(resourceCategoryBack);
        assertThat(resourceCategory.getChildren()).containsOnly(resourceCategoryBack);
        assertThat(resourceCategoryBack.getParent()).isEqualTo(resourceCategory);

        // resourceCategory.removeChildren(resourceCategoryBack);
        assertThat(resourceCategory.getChildren()).doesNotContain(resourceCategoryBack);
        assertThat(resourceCategoryBack.getParent()).isNull();

        resourceCategory.children(new ArrayList<>(Set.of(resourceCategoryBack)));
        assertThat(resourceCategory.getChildren()).containsOnly(resourceCategoryBack);
        assertThat(resourceCategoryBack.getParent()).isEqualTo(resourceCategory);

        resourceCategory.setChildren(new ArrayList<>());
        assertThat(resourceCategory.getChildren()).doesNotContain(resourceCategoryBack);
        assertThat(resourceCategoryBack.getParent()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        ResourceCategory resourceCategory = getResourceCategoryRandomSampleGenerator();
        ResourceCategory resourceCategoryBack = getResourceCategoryRandomSampleGenerator();

        resourceCategory.setParent(resourceCategoryBack);
        assertThat(resourceCategory.getParent()).isEqualTo(resourceCategoryBack);

        resourceCategory.parent(null);
        assertThat(resourceCategory.getParent()).isNull();
    }

    @Test
    void imagesTest() throws Exception {
        ResourceCategory resourceCategory = getResourceCategoryRandomSampleGenerator();
        UploadImage uploadImageBack = getUploadImageRandomSampleGenerator();

        // resourceCategory.addImages(uploadImageBack);
        assertThat(resourceCategory.getImages()).containsOnly(uploadImageBack);
        assertThat(uploadImageBack.getCategory()).isEqualTo(resourceCategory);

        // resourceCategory.removeImages(uploadImageBack);
        assertThat(resourceCategory.getImages()).doesNotContain(uploadImageBack);
        assertThat(uploadImageBack.getCategory()).isNull();

        resourceCategory.images(new ArrayList<>(Set.of(uploadImageBack)));
        assertThat(resourceCategory.getImages()).containsOnly(uploadImageBack);
        assertThat(uploadImageBack.getCategory()).isEqualTo(resourceCategory);

        resourceCategory.setImages(new ArrayList<>());
        assertThat(resourceCategory.getImages()).doesNotContain(uploadImageBack);
        assertThat(uploadImageBack.getCategory()).isNull();
    }

    @Test
    void filesTest() throws Exception {
        ResourceCategory resourceCategory = getResourceCategoryRandomSampleGenerator();
        UploadFile uploadFileBack = getUploadFileRandomSampleGenerator();

        // resourceCategory.addFiles(uploadFileBack);
        assertThat(resourceCategory.getFiles()).containsOnly(uploadFileBack);
        assertThat(uploadFileBack.getCategory()).isEqualTo(resourceCategory);

        // resourceCategory.removeFiles(uploadFileBack);
        assertThat(resourceCategory.getFiles()).doesNotContain(uploadFileBack);
        assertThat(uploadFileBack.getCategory()).isNull();

        resourceCategory.files(new ArrayList<>(Set.of(uploadFileBack)));
        assertThat(resourceCategory.getFiles()).containsOnly(uploadFileBack);
        assertThat(uploadFileBack.getCategory()).isEqualTo(resourceCategory);

        resourceCategory.setFiles(new ArrayList<>());
        assertThat(resourceCategory.getFiles()).doesNotContain(uploadFileBack);
        assertThat(uploadFileBack.getCategory()).isNull();
    }
}
