package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.ResourceCategoryTestSamples.*;
import static com.begcode.monolith.domain.UploadImageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UploadImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadImage.class);
        UploadImage uploadImage1 = getUploadImageSample1();
        UploadImage uploadImage2 = new UploadImage();
        assertThat(uploadImage1).isNotEqualTo(uploadImage2);

        uploadImage2.setId(uploadImage1.getId());
        assertThat(uploadImage1).isEqualTo(uploadImage2);

        uploadImage2 = getUploadImageSample2();
        assertThat(uploadImage1).isNotEqualTo(uploadImage2);
    }

    @Test
    void categoryTest() throws Exception {
        UploadImage uploadImage = getUploadImageRandomSampleGenerator();
        ResourceCategory resourceCategoryBack = getResourceCategoryRandomSampleGenerator();

        uploadImage.setCategory(resourceCategoryBack);
        assertThat(uploadImage.getCategory()).isEqualTo(resourceCategoryBack);

        uploadImage.category(null);
        assertThat(uploadImage.getCategory()).isNull();
    }
}
