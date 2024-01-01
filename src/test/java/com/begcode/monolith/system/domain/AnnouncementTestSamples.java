package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnnouncementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Announcement getAnnouncementSample1() {
        return new Announcement().id(1L).title("title1").senderId(1L).businessId(1L).openPage("openPage1").createdBy(1L).lastModifiedBy(1L);
    }

    public static Announcement getAnnouncementSample2() {
        return new Announcement().id(2L).title("title2").senderId(2L).businessId(2L).openPage("openPage2").createdBy(2L).lastModifiedBy(2L);
    }

    public static Announcement getAnnouncementRandomSampleGenerator() {
        return new Announcement()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .senderId(longCount.incrementAndGet())
            .businessId(longCount.incrementAndGet())
            .openPage(UUID.randomUUID().toString())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
