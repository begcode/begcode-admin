package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.service.dto.AdminUserDTO;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.dto.UserDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
    default List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::toDto).collect(Collectors.toList());
    }

    default List<AdminUserDTO> usersToAdminUserDTOs(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).collect(Collectors.toList());
    }

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "nameList")
    AdminUserDTO userToAdminUserDTO(User user);

    default List<User> userDTOsToUsers(List<AdminUserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }

    @Mapping(target = "authorities", ignore = true)
    User userDTOToUser(AdminUserDTO userDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    default UserDTO toDtoId(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        return userDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    default Set<UserDTO> toDtoIdSet(Set<User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserDTO> userSet = new LinkedHashSet<>();
        for (User userEntity : users) {
            userSet.add(this.toDtoId(userEntity));
        }

        return userSet;
    }

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    default UserDTO toDtoLogin(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        return userDto;
    }

    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    default Set<UserDTO> toDtoLoginSet(Set<User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserDTO> userSet = new LinkedHashSet<>();
        for (User userEntity : users) {
            userSet.add(this.toDtoLogin(userEntity));
        }

        return userSet;
    }

    @Named("firstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    default UserDTO toDtoFirstName(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        return userDto;
    }

    @Named("authorityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    default AuthorityDTO toDtoName(Authority authority) {
        if (Objects.isNull(authority)) {
            return null;
        }
        AuthorityDTO result = new AuthorityDTO();
        result.setId(authority.getId());
        result.setName(authority.getName());
        return result;
    }

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    default List<AuthorityDTO> toDtoNameList(List<Authority> authorities) {
        if (authorities == null) {
            return Collections.emptyList();
        }
        List<AuthorityDTO> authorityList = new ArrayList<>();
        for (Authority authorityEntity : authorities) {
            authorityList.add(this.toDtoName(authorityEntity));
        }
        return authorityList;
    }

    UserDTO toDto(User user);
}
