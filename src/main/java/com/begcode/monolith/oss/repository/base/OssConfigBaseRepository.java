package com.begcode.monolith.oss.repository.base;

import com.begcode.monolith.oss.domain.OssConfig;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the OssConfig entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface OssConfigBaseRepository<E extends OssConfig> extends BaseCrudMapper<OssConfig> {
    default List<OssConfig> findAll() {
        return this.selectList(null);
    }

    default Optional<OssConfig> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
