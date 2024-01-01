package com.begcode.monolith.repository.base;

import com.begcode.monolith.domain.Position;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the Position entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface PositionBaseRepository<E extends Position> extends BaseCrudMapper<Position> {
    default List<Position> findAll() {
        return this.selectList(null);
    }

    default Optional<Position> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
