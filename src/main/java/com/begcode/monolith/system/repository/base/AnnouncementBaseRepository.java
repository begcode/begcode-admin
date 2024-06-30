package com.begcode.monolith.system.repository.base;

import com.begcode.monolith.system.domain.Announcement;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the Announcement entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface AnnouncementBaseRepository<E extends Announcement> extends BaseCrudMapper<Announcement> {
    default List<Announcement> findAll() {
        return this.selectList(null);
    }

    default Optional<Announcement> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default Announcement saveAndGet(Announcement announcement) {
        if (Objects.nonNull(announcement.getId())) {
            this.updateById(announcement);
        } else {
            this.insert(announcement);
        }
        return this.selectById(announcement.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
