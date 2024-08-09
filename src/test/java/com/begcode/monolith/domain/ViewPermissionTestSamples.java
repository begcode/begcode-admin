package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ViewPermissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ViewPermission getViewPermissionSample1() {
        return new ViewPermission()
            .id(1L)
            .text("text1")
            .localeKey("localeKey1")
            .link("link1")
            .externalLink("externalLink1")
            .icon("icon1")
            .code("code1")
            .description("description1")
            .order(1)
            .apiPermissionCodes("apiPermissionCodes1")
            .componentFile("componentFile1")
            .redirect("redirect1");
    }

    public static ViewPermission getViewPermissionSample2() {
        return new ViewPermission()
            .id(2L)
            .text("text2")
            .localeKey("localeKey2")
            .link("link2")
            .externalLink("externalLink2")
            .icon("icon2")
            .code("code2")
            .description("description2")
            .order(2)
            .apiPermissionCodes("apiPermissionCodes2")
            .componentFile("componentFile2")
            .redirect("redirect2");
    }

    public static ViewPermission getViewPermissionRandomSampleGenerator() {
        return new ViewPermission()
            .id(longCount.incrementAndGet())
            .text(UUID.randomUUID().toString())
            .localeKey(UUID.randomUUID().toString())
            .link(UUID.randomUUID().toString())
            .externalLink(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .order(intCount.incrementAndGet())
            .apiPermissionCodes(UUID.randomUUID().toString())
            .componentFile(UUID.randomUUID().toString())
            .redirect(UUID.randomUUID().toString());
    }
}
