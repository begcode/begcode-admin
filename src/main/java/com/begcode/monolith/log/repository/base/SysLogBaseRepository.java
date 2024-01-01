package com.begcode.monolith.log.repository.base;

import com.begcode.monolith.log.domain.SysLog;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the SysLog entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface SysLogBaseRepository<E extends SysLog> extends BaseCrudMapper<SysLog> {
    default List<SysLog> findAll() {
        return this.selectList(null);
    }

    default Optional<SysLog> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
