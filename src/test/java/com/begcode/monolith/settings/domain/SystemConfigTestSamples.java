package com.begcode.monolith.settings.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SystemConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SystemConfig getSystemConfigSample1() {
        return new SystemConfig()
            .id(1L)
            .categoryName("categoryName1")
            .categoryKey("categoryKey1")
            .sortValue(1)
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static SystemConfig getSystemConfigSample2() {
        return new SystemConfig()
            .id(2L)
            .categoryName("categoryName2")
            .categoryKey("categoryKey2")
            .sortValue(2)
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static SystemConfig getSystemConfigRandomSampleGenerator() {
        return new SystemConfig()
            .id(longCount.incrementAndGet())
            .categoryName(UUID.randomUUID().toString())
            .categoryKey(UUID.randomUUID().toString())
            .sortValue(intCount.incrementAndGet())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
