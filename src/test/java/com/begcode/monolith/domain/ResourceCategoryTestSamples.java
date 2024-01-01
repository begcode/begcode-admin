package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ResourceCategory getResourceCategorySample1() {
        return new ResourceCategory().id(1L).title("title1").code("code1").orderNumber(1);
    }

    public static ResourceCategory getResourceCategorySample2() {
        return new ResourceCategory().id(2L).title("title2").code("code2").orderNumber(2);
    }

    public static ResourceCategory getResourceCategoryRandomSampleGenerator() {
        return new ResourceCategory()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .orderNumber(intCount.incrementAndGet());
    }
}
