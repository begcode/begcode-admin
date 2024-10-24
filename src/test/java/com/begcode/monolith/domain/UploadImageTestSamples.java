package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UploadImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UploadImage getUploadImageSample1() {
        return new UploadImage()
            .id(1L)
            .fullName("fullName1")
            .businessTitle("businessTitle1")
            .businessDesc("businessDesc1")
            .businessStatus("businessStatus1")
            .url("url1")
            .name("name1")
            .ext("ext1")
            .type("type1")
            .path("path1")
            .folder("folder1")
            .fileSize(1L)
            .ownerEntityName("ownerEntityName1")
            .ownerEntityId(1L)
            .smartUrl("smartUrl1")
            .mediumUrl("mediumUrl1")
            .referenceCount(1L)
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static UploadImage getUploadImageSample2() {
        return new UploadImage()
            .id(2L)
            .fullName("fullName2")
            .businessTitle("businessTitle2")
            .businessDesc("businessDesc2")
            .businessStatus("businessStatus2")
            .url("url2")
            .name("name2")
            .ext("ext2")
            .type("type2")
            .path("path2")
            .folder("folder2")
            .fileSize(2L)
            .ownerEntityName("ownerEntityName2")
            .ownerEntityId(2L)
            .smartUrl("smartUrl2")
            .mediumUrl("mediumUrl2")
            .referenceCount(2L)
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static UploadImage getUploadImageRandomSampleGenerator() {
        return new UploadImage()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .businessTitle(UUID.randomUUID().toString())
            .businessDesc(UUID.randomUUID().toString())
            .businessStatus(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .ext(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .folder(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .ownerEntityName(UUID.randomUUID().toString())
            .ownerEntityId(longCount.incrementAndGet())
            .smartUrl(UUID.randomUUID().toString())
            .mediumUrl(UUID.randomUUID().toString())
            .referenceCount(longCount.incrementAndGet())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
