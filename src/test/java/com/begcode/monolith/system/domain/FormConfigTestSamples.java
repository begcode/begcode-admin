package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FormConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FormConfig getFormConfigSample1() {
        return new FormConfig().id(1L).formKey("formKey1").formName("formName1").createdBy(1L).lastModifiedBy(1L);
    }

    public static FormConfig getFormConfigSample2() {
        return new FormConfig().id(2L).formKey("formKey2").formName("formName2").createdBy(2L).lastModifiedBy(2L);
    }

    public static FormConfig getFormConfigRandomSampleGenerator() {
        return new FormConfig()
            .id(longCount.incrementAndGet())
            .formKey(UUID.randomUUID().toString())
            .formName(UUID.randomUUID().toString())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
