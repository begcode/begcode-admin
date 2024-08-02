package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Department getDepartmentSample1() {
        return new Department()
            .id(1L)
            .name("name1")
            .code("code1")
            .address("address1")
            .phoneNum("phoneNum1")
            .logo("logo1")
            .contact("contact1");
    }

    public static Department getDepartmentSample2() {
        return new Department()
            .id(2L)
            .name("name2")
            .code("code2")
            .address("address2")
            .phoneNum("phoneNum2")
            .logo("logo2")
            .contact("contact2");
    }

    public static Department getDepartmentRandomSampleGenerator() {
        return new Department()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phoneNum(UUID.randomUUID().toString())
            .logo(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString());
    }
}
