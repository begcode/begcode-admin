package com.begcode.monolith.repository.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.domain.ViewPermission;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the ViewPermission entity.
 */
@NoRepositoryBean
public interface ViewPermissionBaseRepository<E extends ViewPermission> extends BaseCrudMapper<ViewPermission> {
    default Optional<ViewPermission> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(viewPermission -> {
            Binder.bindRelations(viewPermission, new String[] { "children", "parent", "authorities" });
            return viewPermission;
        });
    }

    default List<ViewPermission> findAll() {
        return this.selectList(null);
    }

    default Optional<ViewPermission> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from view_permission view_permission where view_permission.parent_id = #{parentId}")
    void deleteAllByParentId(@Param("parentId") Long parentId);

    default IPage<ViewPermission> findAllByParentIsNull(IPage<ViewPermission> pageable) {
        return this.selectPage(pageable, new QueryWrapper<ViewPermission>().isNull("parent_id"));
    }

    default ViewPermission saveAndGet(ViewPermission viewPermission) {
        if (Objects.nonNull(viewPermission.getId())) {
            this.updateById(viewPermission);
        } else {
            this.insert(viewPermission);
        }
        return this.selectById(viewPermission.getId());
    }

    @Select(
        "select self.* from view_permission self left join rel_jhi_authority__view_permissions avp on self.id = avp.view_permissions_id " +
        "left join rel_jhi_user__authorities jua on jua.authorities_id = avp.jhi_authority_id " +
        "where jua.jhi_user_id = #{userId}"
    )
    List<ViewPermission> findAllViewPermissionsByUserId(@Param("userId") Long userId);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
