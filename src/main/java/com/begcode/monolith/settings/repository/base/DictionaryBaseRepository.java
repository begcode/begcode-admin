package com.begcode.monolith.settings.repository.base;

import com.begcode.monolith.settings.domain.Dictionary;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the Dictionary entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface DictionaryBaseRepository<E extends Dictionary> extends BaseCrudMapper<Dictionary> {
    default List<Dictionary> findAll() {
        return this.selectList(null);
    }

    default Optional<Dictionary> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default Dictionary saveAndGet(Dictionary dictionary) {
        if (Objects.nonNull(dictionary.getId())) {
            this.updateById(dictionary);
        } else {
            this.insert(dictionary);
        }
        return this.selectById(dictionary.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
