package com.begcode.monolith.config;

import com.begcode.monolith.security.MyUserDetails;
import java.util.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public final class WithMockMyUserSecurityContextFactory implements WithSecurityContextFactory<WithMockMyUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockMyUser withMyUser) {
        String username = StringUtils.hasLength(withMyUser.username()) ? withMyUser.username() : withMyUser.value();
        Assert.notNull(username, () -> withMyUser + " cannot have null username on both username and value properties");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : withMyUser.authorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        if (grantedAuthorities.isEmpty()) {
            for (String role : withMyUser.roles()) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        } else if (!(withMyUser.roles().length == 1 && "USER".equals(withMyUser.roles()[0]))) {
            throw new IllegalStateException(
                "You cannot define roles attribute " +
                Arrays.asList(withMyUser.roles()) +
                " with authorities attribute " +
                Arrays.asList(withMyUser.authorities())
            );
        }
        MyUserDetails principal = new MyUserDetails(
            username,
            withMyUser.password(),
            true,
            true,
            true,
            true,
            grantedAuthorities,
            Long.parseLong(withMyUser.id())
        );
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
            principal,
            principal.getPassword(),
            principal.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
