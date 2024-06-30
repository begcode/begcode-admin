package com.begcode.monolith.settings.repository.base;

import com.begcode.monolith.settings.domain.SiteConfig;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the SiteConfig entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface SiteConfigBaseRepository<E extends SiteConfig> extends BaseCrudMapper<SiteConfig> {
    default List<SiteConfig> findAll() {
        return this.selectList(null);
    }

    default Optional<SiteConfig> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default SiteConfig saveAndGet(SiteConfig siteConfig) {
        if (Objects.nonNull(siteConfig.getId())) {
            this.updateById(siteConfig);
        } else {
            this.insert(siteConfig);
        }
        return this.selectById(siteConfig.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
