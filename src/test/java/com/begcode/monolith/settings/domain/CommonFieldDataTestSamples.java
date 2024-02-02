package com.begcode.monolith.settings.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CommonFieldDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CommonFieldData getCommonFieldDataSample1() {
        return new CommonFieldData()
            .id(1L)
            .name("name1")
            .value("value1")
            .label("label1")
            .remark("remark1")
            .sortValue(1)
            .ownerEntityName("ownerEntityName1")
            .ownerEntityId(1L);
    }

    public static CommonFieldData getCommonFieldDataSample2() {
        return new CommonFieldData()
            .id(2L)
            .name("name2")
            .value("value2")
            .label("label2")
            .remark("remark2")
            .sortValue(2)
            .ownerEntityName("ownerEntityName2")
            .ownerEntityId(2L);
    }

    public static CommonFieldData getCommonFieldDataRandomSampleGenerator() {
        return new CommonFieldData()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString())
            .label(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString())
            .sortValue(intCount.incrementAndGet())
            .ownerEntityName(UUID.randomUUID().toString())
            .ownerEntityId(longCount.incrementAndGet());
    }
}
