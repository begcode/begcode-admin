package com.begcode.monolith.settings.repository.base;

import com.begcode.monolith.settings.domain.SystemConfig;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the SystemConfig entity.
 */
@NoRepositoryBean
public interface SystemConfigBaseRepository<E extends SystemConfig> extends BaseCrudMapper<SystemConfig> {
    default Optional<SystemConfig> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(systemConfig -> {
            Binder.bindRelations(systemConfig, new String[] { "items" });
            return systemConfig;
        });
    }

    default List<SystemConfig> findAll() {
        return this.selectList(null);
    }

    default Optional<SystemConfig> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default SystemConfig saveAndGet(SystemConfig systemConfig) {
        if (Objects.nonNull(systemConfig.getId())) {
            this.updateById(systemConfig);
        } else {
            this.insert(systemConfig);
        }
        return this.selectById(systemConfig.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
