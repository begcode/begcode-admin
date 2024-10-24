package com.begcode.monolith.security;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.repository.UserRepository;
import com.begcode.monolith.util.MobileNumberValidator;
import com.diboot.core.binding.Binder;
import java.util.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return this.userRepository.findOneByEmailIgnoreCase(login)
                .map(user -> {
                    Binder.bindRelations(user);
                    return user;
                })
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }
        if (MobileNumberValidator.isValidChinaMobileNumber(login)) {
            Optional<UserDetails> result =
                this.userRepository.findByMobile(login)
                    .map(user -> {
                        Binder.bindRelations(user);
                        return user;
                    })
                    .map(user -> createSpringSecurityUser(user.getLogin(), user));
            if (result.isPresent()) {
                return result.get();
            }
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return this.userRepository.findOneByLogin(lowercaseLogin)
            .map(user -> {
                Binder.bindRelations(user);
                return user;
            })
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<SimpleGrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getCode)
            .map(SimpleGrantedAuthority::new)
            .toList();
        return new MyUserDetails(user.getLogin(), user.getPassword(), grantedAuthorities, user.getId());
    }
}
