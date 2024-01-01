package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PositionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Position getPositionSample1() {
        return new Position().id(1L).code("code1").name("name1").sortNo(1).description("description1");
    }

    public static Position getPositionSample2() {
        return new Position().id(2L).code("code2").name("name2").sortNo(2).description("description2");
    }

    public static Position getPositionRandomSampleGenerator() {
        return new Position()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .sortNo(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
