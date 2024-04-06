package com.begcode.monolith.settings.repository.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.settings.domain.RegionCode;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the RegionCode entity.
 */
@NoRepositoryBean
public interface RegionCodeBaseRepository<E extends RegionCode> extends BaseCrudMapper<RegionCode> {
    default Optional<RegionCode> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(regionCode -> {
            Binder.bindRelations(regionCode, new String[] { "children", "parent" });
            return regionCode;
        });
    }

    default List<RegionCode> findAll() {
        return this.selectList(null);
    }

    default Optional<RegionCode> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from region_code regionCode where regionCode.parent = #{parentId}")
    void deleteAllByParentId(@Param("parentId") Long parentId);

    default IPage<RegionCode> findAllByParentIsNull(IPage<RegionCode> pageable) {
        return this.selectPage(pageable, new QueryWrapper<RegionCode>().isNull("parent_id"));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
