package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SmsSupplierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SmsSupplier getSmsSupplierSample1() {
        return new SmsSupplier().id(1L).configData("configData1").signName("signName1").remark("remark1");
    }

    public static SmsSupplier getSmsSupplierSample2() {
        return new SmsSupplier().id(2L).configData("configData2").signName("signName2").remark("remark2");
    }

    public static SmsSupplier getSmsSupplierRandomSampleGenerator() {
        return new SmsSupplier()
            .id(longCount.incrementAndGet())
            .configData(UUID.randomUUID().toString())
            .signName(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString());
    }
}
