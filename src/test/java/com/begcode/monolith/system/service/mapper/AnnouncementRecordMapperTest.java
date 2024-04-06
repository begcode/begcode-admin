package com.begcode.monolith.system.service.mapper;

import static com.begcode.monolith.system.domain.AnnouncementRecordAsserts.*;
import static com.begcode.monolith.system.domain.AnnouncementRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnouncementRecordMapperTest {

    private AnnouncementRecordMapper announcementRecordMapper;

    @BeforeEach
    void setUp() {
        announcementRecordMapper = new AnnouncementRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnnouncementRecordSample1();
        var actual = announcementRecordMapper.toEntity(announcementRecordMapper.toDto(expected));
        assertAnnouncementRecordAllPropertiesEquals(expected, actual);
    }
}
