package com.begcode.monolith.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static User getUserSample1() {
        return new User()
            .id(1L)
            .login("login1")
            .password("password1")
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .mobile("mobile1")
            .langKey("langKey1")
            .imageUrl("imageUrl1")
            .activationKey("activationKey1")
            .resetKey("resetKey1")
            .createdBy(1L)
            .lastModifiedBy(1L);
    }

    public static User getUserSample2() {
        return new User()
            .id(2L)
            .login("login2")
            .password("password2")
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .mobile("mobile2")
            .langKey("langKey2")
            .imageUrl("imageUrl2")
            .activationKey("activationKey2")
            .resetKey("resetKey2")
            .createdBy(2L)
            .lastModifiedBy(2L);
    }

    public static User getUserRandomSampleGenerator() {
        return new User()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .mobile(UUID.randomUUID().toString())
            .langKey(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .activationKey(UUID.randomUUID().toString())
            .resetKey(UUID.randomUUID().toString())
            .createdBy(longCount.incrementAndGet())
            .lastModifiedBy(longCount.incrementAndGet());
    }
}
