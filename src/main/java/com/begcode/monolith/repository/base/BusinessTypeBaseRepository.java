package com.begcode.monolith.repository.base;

import com.begcode.monolith.domain.BusinessType;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the BusinessType entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface BusinessTypeBaseRepository<E extends BusinessType> extends BaseCrudMapper<BusinessType> {
    default List<BusinessType> findAll() {
        return this.selectList(null);
    }

    default Optional<BusinessType> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default BusinessType saveAndGet(BusinessType businessType) {
        if (Objects.nonNull(businessType.getId())) {
            this.updateById(businessType);
        } else {
            this.insert(businessType);
        }
        return this.selectById(businessType.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
