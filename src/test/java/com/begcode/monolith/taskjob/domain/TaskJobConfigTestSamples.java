package com.begcode.monolith.taskjob.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskJobConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TaskJobConfig getTaskJobConfigSample1() {
        return new TaskJobConfig()
            .id(1L)
            .name("name1")
            .jobClassName("jobClassName1")
            .cronExpression("cronExpression1")
            .parameter("parameter1")
            .description("description1")
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static TaskJobConfig getTaskJobConfigSample2() {
        return new TaskJobConfig()
            .id(2L)
            .name("name2")
            .jobClassName("jobClassName2")
            .cronExpression("cronExpression2")
            .parameter("parameter2")
            .description("description2")
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static TaskJobConfig getTaskJobConfigRandomSampleGenerator() {
        return new TaskJobConfig()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .jobClassName(UUID.randomUUID().toString())
            .cronExpression(UUID.randomUUID().toString())
            .parameter(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
