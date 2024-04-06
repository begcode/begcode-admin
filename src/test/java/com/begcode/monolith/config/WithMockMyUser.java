package com.begcode.monolith.config;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockMyUserSecurityContextFactory.class)
public @interface WithMockMyUser {
    String id() default "2";

    String value() default "user";

    String username() default "";

    String[] roles() default { "USER" };

    String[] authorities() default {};

    String password() default "password";

    @AliasFor(annotation = WithSecurityContext.class)
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}
