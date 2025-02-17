package com.begcode.monolith.settings.repository.base;

import com.begcode.monolith.settings.domain.SysFillRule;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the SysFillRule entity.
 */
@NoRepositoryBean
public interface SysFillRuleBaseRepository<E extends SysFillRule> extends BaseCrudMapper<SysFillRule> {
    default Optional<SysFillRule> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(sysFillRule -> {
            Binder.bindRelations(sysFillRule, new String[] { "ruleItems" });
            return sysFillRule;
        });
    }

    default List<SysFillRule> findAll() {
        return this.selectList(null);
    }

    default Optional<SysFillRule> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default SysFillRule saveAndGet(SysFillRule sysFillRule) {
        if (Objects.nonNull(sysFillRule.getId())) {
            this.updateById(sysFillRule);
        } else {
            this.insert(sysFillRule);
        }
        return this.selectById(sysFillRule.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
