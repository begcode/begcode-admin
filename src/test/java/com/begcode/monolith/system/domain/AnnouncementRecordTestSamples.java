package com.begcode.monolith.system.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AnnouncementRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AnnouncementRecord getAnnouncementRecordSample1() {
        return new AnnouncementRecord().id(1L).anntId(1L).userId(1L).createdBy(1L).lastModifiedBy(1L);
    }

    public static AnnouncementRecord getAnnouncementRecordSample2() {
        return new AnnouncementRecord().id(2L).anntId(2L).userId(2L).createdBy(2L).lastModifiedBy(2L);
    }

    public static AnnouncementRecord getAnnouncementRecordRandomSampleGenerator() {
        return new AnnouncementRecord()
            .id(longCount.incrementAndGet())
            .anntId(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
