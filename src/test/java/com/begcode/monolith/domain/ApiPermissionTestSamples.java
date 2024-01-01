package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApiPermissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApiPermission getApiPermissionSample1() {
        return new ApiPermission()
            .id(1L)
            .serviceName("serviceName1")
            .name("name1")
            .code("code1")
            .description("description1")
            .method("method1")
            .url("url1");
    }

    public static ApiPermission getApiPermissionSample2() {
        return new ApiPermission()
            .id(2L)
            .serviceName("serviceName2")
            .name("name2")
            .code("code2")
            .description("description2")
            .method("method2")
            .url("url2");
    }

    public static ApiPermission getApiPermissionRandomSampleGenerator() {
        return new ApiPermission()
            .id(longCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .method(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
