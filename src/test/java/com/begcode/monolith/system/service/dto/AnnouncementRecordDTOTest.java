package com.begcode.monolith.system.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnouncementRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnouncementRecordDTO.class);
        AnnouncementRecordDTO announcementRecordDTO1 = new AnnouncementRecordDTO();
        announcementRecordDTO1.setId(1L);
        AnnouncementRecordDTO announcementRecordDTO2 = new AnnouncementRecordDTO();
        assertThat(announcementRecordDTO1).isNotEqualTo(announcementRecordDTO2);
        announcementRecordDTO2.setId(announcementRecordDTO1.getId());
        assertThat(announcementRecordDTO1).isEqualTo(announcementRecordDTO2);
        announcementRecordDTO2.setId(2L);
        assertThat(announcementRecordDTO1).isNotEqualTo(announcementRecordDTO2);
        announcementRecordDTO1.setId(null);
        assertThat(announcementRecordDTO1).isNotEqualTo(announcementRecordDTO2);
    }
}
