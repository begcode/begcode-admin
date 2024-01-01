package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UReportFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UReportFile getUReportFileSample1() {
        return new UReportFile().id(1L).name("name1");
    }

    public static UReportFile getUReportFileSample2() {
        return new UReportFile().id(2L).name("name2");
    }

    public static UReportFile getUReportFileRandomSampleGenerator() {
        return new UReportFile().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
