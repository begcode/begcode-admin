package com.begcode.monolith.system.service.mapper;

import static com.begcode.monolith.system.domain.AnnouncementAsserts.*;
import static com.begcode.monolith.system.domain.AnnouncementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnouncementMapperTest {

    private AnnouncementMapper announcementMapper;

    @BeforeEach
    void setUp() {
        announcementMapper = new AnnouncementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnnouncementSample1();
        var actual = announcementMapper.toEntity(announcementMapper.toDto(expected));
        assertAnnouncementAllPropertiesEquals(expected, actual);
    }
}
