package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Authority getAuthoritySample1() {
        return new Authority().id(1L).name("name1").code("code1").info("info1").order(1);
    }

    public static Authority getAuthoritySample2() {
        return new Authority().id(2L).name("name2").code("code2").info("info2").order(2);
    }

    public static Authority getAuthorityRandomSampleGenerator() {
        return new Authority()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .info(UUID.randomUUID().toString())
            .order(intCount.incrementAndGet());
    }
}
