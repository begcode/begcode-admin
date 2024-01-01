package com.begcode.monolith.settings.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RegionCodeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RegionCode getRegionCodeSample1() {
        return new RegionCode()
            .id(1L)
            .name("name1")
            .areaCode("areaCode1")
            .cityCode("cityCode1")
            .mergerName("mergerName1")
            .shortName("shortName1")
            .zipCode("zipCode1");
    }

    public static RegionCode getRegionCodeSample2() {
        return new RegionCode()
            .id(2L)
            .name("name2")
            .areaCode("areaCode2")
            .cityCode("cityCode2")
            .mergerName("mergerName2")
            .shortName("shortName2")
            .zipCode("zipCode2");
    }

    public static RegionCode getRegionCodeRandomSampleGenerator() {
        return new RegionCode()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .areaCode(UUID.randomUUID().toString())
            .cityCode(UUID.randomUUID().toString())
            .mergerName(UUID.randomUUID().toString())
            .shortName(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString());
    }
}
