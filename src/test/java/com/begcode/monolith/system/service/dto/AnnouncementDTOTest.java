package com.begcode.monolith.system.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnouncementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnouncementDTO.class);
        AnnouncementDTO announcementDTO1 = new AnnouncementDTO();
        announcementDTO1.setId(1L);
        AnnouncementDTO announcementDTO2 = new AnnouncementDTO();
        assertThat(announcementDTO1).isNotEqualTo(announcementDTO2);
        announcementDTO2.setId(announcementDTO1.getId());
        assertThat(announcementDTO1).isEqualTo(announcementDTO2);
        announcementDTO2.setId(2L);
        assertThat(announcementDTO1).isNotEqualTo(announcementDTO2);
        announcementDTO1.setId(null);
        assertThat(announcementDTO1).isNotEqualTo(announcementDTO2);
    }
}
