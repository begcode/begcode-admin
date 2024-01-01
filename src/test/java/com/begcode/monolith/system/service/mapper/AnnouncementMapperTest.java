package com.begcode.monolith.system.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AnnouncementMapperTest {

    private AnnouncementMapper announcementMapper;

    @BeforeEach
    public void setUp() {
        announcementMapper = new AnnouncementMapperImpl();
    }
}
