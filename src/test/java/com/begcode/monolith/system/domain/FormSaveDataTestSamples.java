package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FormSaveDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FormSaveData getFormSaveDataSample1() {
        return new FormSaveData().id(1L).createdBy(1L).lastModifiedBy(1L);
    }

    public static FormSaveData getFormSaveDataSample2() {
        return new FormSaveData().id(2L).createdBy(2L).lastModifiedBy(2L);
    }

    public static FormSaveData getFormSaveDataRandomSampleGenerator() {
        return new FormSaveData()
            .id(longCount.incrementAndGet())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
