package com.begcode.monolith.repository.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the ApiPermission entity.
 */
@NoRepositoryBean
public interface ApiPermissionBaseRepository<E extends ApiPermission> extends BaseCrudMapper<ApiPermission> {
    default Optional<ApiPermission> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(apiPermission -> {
            Binder.bindRelations(apiPermission, new String[] { "children", "parent", "authorities" });
            return apiPermission;
        });
    }

    default List<ApiPermission> findAll() {
        return this.selectList(null);
    }

    default Optional<ApiPermission> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from api_permission apiPermission where apiPermission.parent = #{parentId}")
    void deleteAllByParentId(@Param("parentId") Long parentId);

    default IPage<ApiPermission> findAllByParentIsNull(IPage<ApiPermission> pageable) {
        return this.selectPage(pageable, new QueryWrapper<ApiPermission>().isNull("parent_id"));
    }

    default Optional<ApiPermission> findOneByCode(String groupCode) {
        return Optional.ofNullable(this.selectOne(new QueryWrapper<ApiPermission>().eq("code", groupCode)));
    }

    default List<ApiPermission> findAllByType(ApiPermissionType type) {
        return this.selectList(new LambdaQueryWrapper<ApiPermission>().eq(ApiPermission::getType, type));
    }

    @Select(
        "select self.* from api_permission self left join rel_jhi_authority__api_permissions aap on self.id = aap.api_permissions_id " +
        "left join rel_jhi_user__authorities jua on jua.authorities_id = aap.authority_id " +
        "where jua.jhi_user_id = #{userId}"
    )
    List<ApiPermission> findAllApiPermissionsByCurrentUser(@Param("userId") Long userId);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
