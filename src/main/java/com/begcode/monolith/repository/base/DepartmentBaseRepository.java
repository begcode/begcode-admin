package com.begcode.monolith.repository.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.domain.Department;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the Department entity.
 *
 * When extending this class, extend DepartmentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@NoRepositoryBean
public interface DepartmentBaseRepository<E extends Department> extends BaseCrudMapper<Department> {
    default Optional<Department> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(department -> {
            Binder.bindRelations(department, new String[] { "children", "authorities", "parent", "users" });
            return department;
        });
    }

    default List<Department> findAll() {
        return this.selectList(null);
    }

    default Optional<Department> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from department department where department.parent = #{parentId}")
    void deleteAllByParentId(@Param("parentId") Long parentId);

    default IPage<Department> findAllByParentIsNull(IPage<Department> pageable) {
        return this.selectPage(pageable, new QueryWrapper<Department>().isNull("parent_id"));
    }

    default Department saveAndGet(Department department) {
        if (Objects.nonNull(department.getId())) {
            this.updateById(department);
        } else {
            this.insert(department);
        }
        return this.selectById(department.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
