package com.begcode.monolith.settings.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SysFillRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SysFillRule getSysFillRuleSample1() {
        return new SysFillRule()
            .id(1L)
            .name("name1")
            .code("code1")
            .desc("desc1")
            .seqValue(1L)
            .fillValue("fillValue1")
            .implClass("implClass1")
            .params("params1");
    }

    public static SysFillRule getSysFillRuleSample2() {
        return new SysFillRule()
            .id(2L)
            .name("name2")
            .code("code2")
            .desc("desc2")
            .seqValue(2L)
            .fillValue("fillValue2")
            .implClass("implClass2")
            .params("params2");
    }

    public static SysFillRule getSysFillRuleRandomSampleGenerator() {
        return new SysFillRule()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .desc(UUID.randomUUID().toString())
            .seqValue(longCount.incrementAndGet())
            .fillValue(UUID.randomUUID().toString())
            .implClass(UUID.randomUUID().toString())
            .params(UUID.randomUUID().toString());
    }
}
