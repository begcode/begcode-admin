package com.begcode.monolith.settings.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DictionaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Dictionary getDictionarySample1() {
        return new Dictionary().id(1L).dictName("dictName1").dictKey("dictKey1").sortValue(1);
    }

    public static Dictionary getDictionarySample2() {
        return new Dictionary().id(2L).dictName("dictName2").dictKey("dictKey2").sortValue(2);
    }

    public static Dictionary getDictionaryRandomSampleGenerator() {
        return new Dictionary()
            .id(longCount.incrementAndGet())
            .dictName(UUID.randomUUID().toString())
            .dictKey(UUID.randomUUID().toString())
            .sortValue(intCount.incrementAndGet());
    }
}
