package com.begcode.monolith.log.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SysLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SysLog getSysLogSample1() {
        return new SysLog()
            .id(1L)
            .requestUrl("requestUrl1")
            .logContent("logContent1")
            .userid("userid1")
            .username("username1")
            .ip("ip1")
            .method("method1")
            .requestType("requestType1")
            .costTime(1L)
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static SysLog getSysLogSample2() {
        return new SysLog()
            .id(2L)
            .requestUrl("requestUrl2")
            .logContent("logContent2")
            .userid("userid2")
            .username("username2")
            .ip("ip2")
            .method("method2")
            .requestType("requestType2")
            .costTime(2L)
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static SysLog getSysLogRandomSampleGenerator() {
        return new SysLog()
            .id(longCount.incrementAndGet())
            .requestUrl(UUID.randomUUID().toString())
            .logContent(UUID.randomUUID().toString())
            .userid(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .ip(UUID.randomUUID().toString())
            .method(UUID.randomUUID().toString())
            .requestType(UUID.randomUUID().toString())
            .costTime(longCount.incrementAndGet())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
