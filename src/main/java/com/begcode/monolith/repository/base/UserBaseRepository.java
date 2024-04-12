package com.begcode.monolith.repository.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.domain.User;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import com.diboot.core.mapper.BaseCrudMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the User entity.
 *
 * When extending this class, extend UserRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@NoRepositoryBean
public interface UserBaseRepository<E extends User> extends BaseCrudMapper<User> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    default Optional<User> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(user -> {
            Binder.bindRelations(user, new String[] { "department", "position", "authorities" });
            return user;
        });
    }

    default List<User> findAll() {
        return this.selectList(null);
    }

    default Optional<User> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from jhi_user user where user.department = #{departmentId}")
    void deleteAllByDepartmentId(@Param("departmentId") Long departmentId);

    @Select("delete from jhi_user user where user.position = #{positionId}")
    void deleteAllByPositionId(@Param("positionId") Long positionId);

    //    default Optional<User> findById(Long id) {
    //        return Optional.ofNullable(this.selectById(id));
    //    }

    default Optional<User> findOneByActivationKey(String activationKey) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getActivationKey, activationKey)));
    }

    default List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime) {
        return this.selectList(
                new LambdaQueryWrapper<User>()
                    .eq(User::isActivated, false)
                    .isNotNull(User::getActivationKey)
                    .le(User::getCreatedDate, dateTime)
            );
    }

    default Optional<User> findOneByResetKey(String resetKey) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getResetKey, resetKey)));
    }

    default Optional<User> findOneByEmailIgnoreCase(String email) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email.toLowerCase())));
    }

    default Optional<User> findOneByLogin(String login) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getLogin, login.toLowerCase())));
    }

    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    default Optional<User> findOneWithAuthoritiesByLogin(String login) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getLogin, login.toLowerCase())));
    }

    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    default Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email.toLowerCase())));
    }

    default Optional<User> findByMobile(String mobile) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile)));
    }

    default Boolean existsByMobileAndLoginNot(String mobile, String login) {
        return this.selectCount(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile).ne(User::getLogin, login.toLowerCase())) > 0;
    }

    default IPage<User> findAllByLoginNot(IPage<User> pageable, String login) {
        return this.selectPage(pageable, new LambdaQueryWrapper<User>().ne(User::getLogin, login.toLowerCase()));
    }

    default IPage<User> findAllByIdNotNullAndActivatedIsTrue(IPage<User> pageable) {
        return this.selectPage(pageable, new LambdaQueryWrapper<User>().isNotNull(User::getId).eq(User::isActivated, true));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
