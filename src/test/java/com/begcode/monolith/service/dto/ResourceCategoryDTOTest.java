package com.begcode.monolith.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceCategoryDTO.class);
        ResourceCategoryDTO resourceCategoryDTO1 = new ResourceCategoryDTO();
        resourceCategoryDTO1.setId(1L);
        ResourceCategoryDTO resourceCategoryDTO2 = new ResourceCategoryDTO();
        assertThat(resourceCategoryDTO1).isNotEqualTo(resourceCategoryDTO2);
        resourceCategoryDTO2.setId(resourceCategoryDTO1.getId());
        assertThat(resourceCategoryDTO1).isEqualTo(resourceCategoryDTO2);
        resourceCategoryDTO2.setId(2L);
        assertThat(resourceCategoryDTO1).isNotEqualTo(resourceCategoryDTO2);
        resourceCategoryDTO1.setId(null);
        assertThat(resourceCategoryDTO1).isNotEqualTo(resourceCategoryDTO2);
    }
}
