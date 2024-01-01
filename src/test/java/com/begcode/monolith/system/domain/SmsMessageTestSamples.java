package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SmsMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SmsMessage getSmsMessageSample1() {
        return new SmsMessage()
            .id(1L)
            .title("title1")
            .receiver("receiver1")
            .params("params1")
            .retryNum(1)
            .failResult("failResult1")
            .remark("remark1")
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static SmsMessage getSmsMessageSample2() {
        return new SmsMessage()
            .id(2L)
            .title("title2")
            .receiver("receiver2")
            .params("params2")
            .retryNum(2)
            .failResult("failResult2")
            .remark("remark2")
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static SmsMessage getSmsMessageRandomSampleGenerator() {
        return new SmsMessage()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .receiver(UUID.randomUUID().toString())
            .params(UUID.randomUUID().toString())
            .retryNum(intCount.incrementAndGet())
            .failResult(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
