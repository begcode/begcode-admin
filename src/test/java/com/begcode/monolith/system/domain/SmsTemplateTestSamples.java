package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SmsTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SmsTemplate getSmsTemplateSample1() {
        return new SmsTemplate()
            .id(1L)
            .name("name1")
            .code("code1")
            .content("content1")
            .testJson("testJson1")
            .remark("remark1")
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static SmsTemplate getSmsTemplateSample2() {
        return new SmsTemplate()
            .id(2L)
            .name("name2")
            .code("code2")
            .content("content2")
            .testJson("testJson2")
            .remark("remark2")
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static SmsTemplate getSmsTemplateRandomSampleGenerator() {
        return new SmsTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .testJson(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
