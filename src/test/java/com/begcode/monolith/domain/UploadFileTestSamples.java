package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UploadFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UploadFile getUploadFileSample1() {
        return new UploadFile()
            .id(1L)
            .fullName("fullName1")
            .businessTitle("businessTitle1")
            .businessDesc("businessDesc1")
            .businessStatus("businessStatus1")
            .url("url1")
            .name("name1")
            .thumb("thumb1")
            .fileSize(1L)
            .ext("ext1")
            .type("type1")
            .path("path1")
            .folder("folder1")
            .ownerEntityName("ownerEntityName1")
            .ownerEntityId(1L)
            .referenceCount(1L)
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static UploadFile getUploadFileSample2() {
        return new UploadFile()
            .id(2L)
            .fullName("fullName2")
            .businessTitle("businessTitle2")
            .businessDesc("businessDesc2")
            .businessStatus("businessStatus2")
            .url("url2")
            .name("name2")
            .thumb("thumb2")
            .fileSize(2L)
            .ext("ext2")
            .type("type2")
            .path("path2")
            .folder("folder2")
            .ownerEntityName("ownerEntityName2")
            .ownerEntityId(2L)
            .referenceCount(2L)
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static UploadFile getUploadFileRandomSampleGenerator() {
        return new UploadFile()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .businessTitle(UUID.randomUUID().toString())
            .businessDesc(UUID.randomUUID().toString())
            .businessStatus(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .thumb(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .ext(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .folder(UUID.randomUUID().toString())
            .ownerEntityName(UUID.randomUUID().toString())
            .ownerEntityId(longCount.incrementAndGet())
            .referenceCount(longCount.incrementAndGet())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
