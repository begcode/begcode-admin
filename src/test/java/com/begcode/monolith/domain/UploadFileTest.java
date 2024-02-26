package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.ResourceCategoryTestSamples.*;
import static com.begcode.monolith.domain.UploadFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class UploadFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadFile.class);
        UploadFile uploadFile1 = getUploadFileSample1();
        UploadFile uploadFile2 = new UploadFile();
        assertThat(uploadFile1).isNotEqualTo(uploadFile2);

        uploadFile2.setId(uploadFile1.getId());
        assertThat(uploadFile1).isEqualTo(uploadFile2);

        uploadFile2 = getUploadFileSample2();
        assertThat(uploadFile1).isNotEqualTo(uploadFile2);
    }

    @Test
    void categoryTest() throws Exception {
        UploadFile uploadFile = getUploadFileRandomSampleGenerator();
        ResourceCategory resourceCategoryBack = getResourceCategoryRandomSampleGenerator();

        uploadFile.setCategory(resourceCategoryBack);
        assertThat(uploadFile.getCategory()).isEqualTo(resourceCategoryBack);

        uploadFile.category(null);
        assertThat(uploadFile.getCategory()).isNull();
    }
}
