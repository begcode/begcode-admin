package com.begcode.monolith.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.config.Constants;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.Position;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.repository.AuthorityRepository;
import com.begcode.monolith.repository.UserRepository;
import com.begcode.monolith.security.AuthoritiesConstants;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.service.dto.AdminUserDTO;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.dto.UserDTO;
import com.begcode.monolith.service.mapper.UserMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
public class UserService extends BaseServiceImpl<UserRepository, User> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final List<String> relationNames = Arrays.asList("department", "authorities", "position");

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                saveOrUpdate(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                saveOrUpdate(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordResetByMobile(String mobile) {
        return userRepository
            .findByMobile(mobile)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                saveOrUpdate(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setMobile(userDTO.getMobile());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        List<Authority> authorities = new ArrayList<>();
        authorityRepository.findFirstByCode(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.deleteById(existingUser);
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMobile(userDTO.getMobile());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            List<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityDTO -> {
                    Authority authority = new Authority();
                    authority.setId(authorityDTO.getId());
                    authority.setCode(authorityDTO.getCode());
                    return authority;
                })
                .collect(Collectors.toList());
            user.setAuthorities(authorities);
        }
        userRepository.insert(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.getActivated());
                user.setLangKey(userDTO.getLangKey());
                List<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(AuthorityDTO::getId)
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);

                this.createOrUpdateAndRelatedRelations(user, List.of("authorities", "department", "position"));
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(userMapper::userToAdminUserDTO);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.deleteById(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl, String mobile) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setMobile(mobile);
                user.setImageUrl(imageUrl);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.saveOrUpdate(user);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    public IPage<AdminUserDTO> getAllManagedUsers(IPage<User> pageable) {
        return userRepository.selectPage(pageable, null).convert(userMapper::userToAdminUserDTO);
    }

    public IPage<UserDTO> getAllPublicUsers(IPage<User> pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).convert(userMapper::toDto);
    }

    public Optional<AdminUserDTO> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login).map(userMapper::userToAdminUserDTO);
    }

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.deleteById(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    /**
     * Update mobile for a specific user, and return the modified user.
     *
     * @param mobile mobile to update.
     * @return updated user.
     */
    @Transactional
    public Optional<AdminUserDTO> updateUserMobile(String mobile) {
        return Optional.of(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setMobile(mobile);
                userRepository.save(user);
                return user;
            })
            .map(userMapper::userToAdminUserDTO);
    }

    public Optional<UserDTO> findOneByMobile(String mobile) {
        log.debug("Request to get Person : {}", mobile);
        return userRepository.findByMobile(mobile).map(userMapper::toDto);
    }

    public Boolean existsByMobileAndLoginNot(String mobile, String login) {
        log.debug("Request to get Person : {}", mobile);
        return userRepository.existsByMobileAndLoginNot(mobile, login);
    }

    public Optional<AdminUserDTO> getUserWithAuthorities(Long id) {
        return userRepository
            .findById(id)
            .map(user -> {
                Binder.bindRelations(user);
                return userMapper.userToAdminUserDTO(user);
            });
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDTO update(UserDTO userDTO) {
        log.debug("Request to update User : {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        user.setDepartmentId(Optional.ofNullable(user.getDepartment()).map(Department::getId).orElse(null));
        user.setPositionId(Optional.ofNullable(user.getPosition()).map(Position::getId).orElse(null));
        this.createOrUpdateAndRelatedRelations(user, List.of("authorities"));
        return findOne(user.getId()).orElseThrow();
    }

    public Optional<UserDTO> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        return Optional.ofNullable(userRepository.selectById(id))
            .map(user -> {
                Binder.bindRelations(user);
                return user;
            })
            .map(userMapper::toDto);
    }

    @Transactional
    public void updateBatch(UserDTO changeUserDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeUserDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<User> userList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(userList)) {
                userList.forEach(user -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                user,
                                relationName,
                                BeanUtil.getFieldValue(userMapper.toEntity(changeUserDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(user, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            User byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
            if (relationshipName.equals("authorities")) {
                if (CollectionUtils.isNotEmpty(otherEntityIds)) {
                    List<Long> ids = otherEntityIds.stream().map(Long::valueOf).toList();
                    List<Authority> authorityExist = byId.getAuthorities();
                    if (operateType.equals("add")) {
                        List<Long> collect = ids
                            .stream()
                            .filter(relId -> authorityExist.stream().noneMatch(vp -> vp.getId().equals(relId)))
                            .toList();
                        if (CollectionUtils.isNotEmpty(collect)) {
                            collect.forEach(addId -> authorityExist.add(new Authority().id(addId)));
                            // 更新
                            this.createOrUpdateAndRelatedRelations(byId, List.of("authorities"));
                        }
                    } else if (operateType.equals("delete")) {
                        List<Long> collect = ids
                            .stream()
                            .filter(relId -> authorityExist.stream().anyMatch(vp -> vp.getId().equals(relId)))
                            .toList();
                        if (CollectionUtils.isNotEmpty(collect)) {
                            List<Authority> userAdd = authorityExist.stream().filter(vp -> !collect.contains(vp.getId())).toList();
                            byId.setAuthorities(userAdd);
                            // 更新
                            this.createOrUpdateAndRelatedRelations(byId, List.of("authorities"));
                        }
                    }
                }
            }
        });
    }
}
