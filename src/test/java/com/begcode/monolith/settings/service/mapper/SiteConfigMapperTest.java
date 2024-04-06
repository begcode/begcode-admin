package com.begcode.monolith.settings.service.mapper;

import static com.begcode.monolith.settings.domain.SiteConfigAsserts.*;
import static com.begcode.monolith.settings.domain.SiteConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SiteConfigMapperTest {

    private SiteConfigMapper siteConfigMapper;

    @BeforeEach
    void setUp() {
        siteConfigMapper = new SiteConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSiteConfigSample1();
        var actual = siteConfigMapper.toEntity(siteConfigMapper.toDto(expected));
        assertSiteConfigAllPropertiesEquals(expected, actual);
    }
}
