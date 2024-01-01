package com.begcode.monolith.taskjob.repository.base;

import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the TaskJobConfig entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface TaskJobConfigBaseRepository<E extends TaskJobConfig> extends BaseCrudMapper<TaskJobConfig> {
    default List<TaskJobConfig> findAll() {
        return this.selectList(null);
    }

    default Optional<TaskJobConfig> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
