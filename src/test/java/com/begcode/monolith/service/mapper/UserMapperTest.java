package com.begcode.monolith.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.security.AuthoritiesConstants;
import com.begcode.monolith.service.dto.AdminUserDTO;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.dto.UserDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UserMapper}.
 */
class UserMapperTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final Long DEFAULT_ID = 1L;

    private UserMapper userMapper;
    private User user;
    private AdminUserDTO userDto;

    @BeforeEach
    public void init() {
        userMapper = new UserMapperImpl();
        user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("image_url");
        user.setCreatedBy(DEFAULT_ID);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(DEFAULT_ID);
        user.setLastModifiedDate(Instant.now());
        user.setLangKey("en");

        userDto = userMapper.userToAdminUserDTO(user);
        List<Authority> authorities = new ArrayList<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.setAuthorities(authorities);

        userDto = userMapper.userToAdminUserDTO(user);
    }

    @Test
    void testUserToUserDTO() {
        AdminUserDTO convertedUserDto = userMapper.userToAdminUserDTO(user);

        assertThat(convertedUserDto.getId()).isEqualTo(user.getId());
        assertThat(convertedUserDto.getLogin()).isEqualTo(user.getLogin());
        assertThat(convertedUserDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(convertedUserDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(convertedUserDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(convertedUserDto.getActivated()).isEqualTo(user.getActivated());
        assertThat(convertedUserDto.getImageUrl()).isEqualTo(user.getImageUrl());
        assertThat(convertedUserDto.getCreatedBy()).isEqualTo(user.getCreatedBy());
        assertThat(convertedUserDto.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(convertedUserDto.getLastModifiedBy()).isEqualTo(user.getLastModifiedBy());
        assertThat(convertedUserDto.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(convertedUserDto.getLangKey()).isEqualTo(user.getLangKey());
        assertThat(convertedUserDto.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    void testUserDTOtoUser() {
        User convertedUser = userMapper.userDTOToUser(userDto);

        assertThat(convertedUser.getId()).isEqualTo(userDto.getId());
        assertThat(convertedUser.getLogin()).isEqualTo(userDto.getLogin());
        assertThat(convertedUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(convertedUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(convertedUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(convertedUser.getActivated()).isEqualTo(userDto.getActivated());
        assertThat(convertedUser.getImageUrl()).isEqualTo(userDto.getImageUrl());
        assertThat(convertedUser.getLangKey()).isEqualTo(userDto.getLangKey());
        assertThat(convertedUser.getCreatedBy()).isEqualTo(userDto.getCreatedBy());
        assertThat(convertedUser.getCreatedDate()).isEqualTo(userDto.getCreatedDate());
        assertThat(convertedUser.getLastModifiedBy()).isEqualTo(userDto.getLastModifiedBy());
        assertThat(convertedUser.getLastModifiedDate()).isEqualTo(userDto.getLastModifiedDate());
        assertThat(convertedUser.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    void usersToUserDTOsShouldMapOnlyNonNullUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(null);

        List<UserDTO> userDTOS = userMapper.usersToUserDTOs(users);

        assertThat(userDTOS).isNotEmpty().size().isEqualTo(1);
    }

    @Test
    void userDTOsToUsersShouldMapOnlyNonNullUsers() {
        List<AdminUserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(null);

        List<User> users = userMapper.userDTOsToUsers(usersDto);

        assertThat(users).isNotEmpty().size().isEqualTo(1);
    }

    @Test
    void userDTOsToUsersWithAuthoritiesStringShouldMapToUsersWithAuthoritiesDomain() {
        List<AuthorityDTO> authoritiesAsString = new ArrayList<>();
        authoritiesAsString.add(new AuthorityDTO().code("ADMIN"));
        userDto.setAuthorities(authoritiesAsString);

        List<AdminUserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<User> users = userMapper.userDTOsToUsers(usersDto);

        assertThat(users).isNotEmpty().size().isEqualTo(1);
        assertThat(users.get(0).getAuthorities()).isNotNull();
        assertThat(users.get(0).getAuthorities()).isNotEmpty();
        assertThat(users.get(0).getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }

    @Test
    void userDTOsToUsersMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        userDto.setAuthorities(null);

        List<AdminUserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<User> users = userMapper.userDTOsToUsers(usersDto);

        assertThat(users).isNotEmpty().size().isEqualTo(1);
        assertThat(users.get(0).getAuthorities()).isNotNull();
        assertThat(users.get(0).getAuthorities()).isEmpty();
    }

    @Test
    void userDTOToUserMapWithAuthoritiesStringShouldReturnUserWithAuthorities() {
        List<AuthorityDTO> authoritiesAsString = new ArrayList<>();
        authoritiesAsString.add(new AuthorityDTO().code("ADMIN"));
        userDto.setAuthorities(authoritiesAsString);
        User convertedUser = userMapper.userDTOToUser(userDto);

        assertThat(convertedUser).isNotNull();
        assertThat(convertedUser.getAuthorities()).isNotNull();
        assertThat(convertedUser.getAuthorities()).isNotEmpty();
        assertThat(convertedUser.getAuthorities().iterator().next().getName()).isEqualTo(AuthoritiesConstants.USER);
    }

    @Test
    void userDTOToUserMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        userDto.setAuthorities(null);

        User persistUser = userMapper.userDTOToUser(userDto);

        assertThat(persistUser).isNotNull();
        assertThat(persistUser.getAuthorities()).isNotNull();
        assertThat(persistUser.getAuthorities()).isEmpty();
    }

    @Test
    void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.userDTOToUser(null)).isNull();
    }

    @Test
    void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }
}
