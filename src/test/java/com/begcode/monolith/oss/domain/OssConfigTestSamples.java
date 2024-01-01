package com.begcode.monolith.oss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OssConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OssConfig getOssConfigSample1() {
        return new OssConfig().id(1L).platform("platform1").remark("remark1").configData("configData1");
    }

    public static OssConfig getOssConfigSample2() {
        return new OssConfig().id(2L).platform("platform2").remark("remark2").configData("configData2");
    }

    public static OssConfig getOssConfigRandomSampleGenerator() {
        return new OssConfig()
            .id(longCount.incrementAndGet())
            .platform(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString())
            .configData(UUID.randomUUID().toString());
    }
}
