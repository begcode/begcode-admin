package com.begcode.monolith.settings.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FillRuleItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FillRuleItem getFillRuleItemSample1() {
        return new FillRuleItem()
            .id(1L)
            .sortValue(1)
            .fieldParamValue("fieldParamValue1")
            .datePattern("datePattern1")
            .seqLength(1)
            .seqIncrement(1)
            .seqStartValue(1);
    }

    public static FillRuleItem getFillRuleItemSample2() {
        return new FillRuleItem()
            .id(2L)
            .sortValue(2)
            .fieldParamValue("fieldParamValue2")
            .datePattern("datePattern2")
            .seqLength(2)
            .seqIncrement(2)
            .seqStartValue(2);
    }

    public static FillRuleItem getFillRuleItemRandomSampleGenerator() {
        return new FillRuleItem()
            .id(longCount.incrementAndGet())
            .sortValue(intCount.incrementAndGet())
            .fieldParamValue(UUID.randomUUID().toString())
            .datePattern(UUID.randomUUID().toString())
            .seqLength(intCount.incrementAndGet())
            .seqIncrement(intCount.incrementAndGet())
            .seqStartValue(intCount.incrementAndGet());
    }
}
