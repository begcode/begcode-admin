package com.begcode.monolith.system.repository.base;

import com.begcode.monolith.system.domain.FormConfig;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the FormConfig entity.
 */
@NoRepositoryBean
public interface FormConfigBaseRepository<E extends FormConfig> extends BaseCrudMapper<FormConfig> {
    default Optional<FormConfig> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(formConfig -> {
            Binder.bindRelations(formConfig, new String[] { "businessType" });
            return formConfig;
        });
    }

    default List<FormConfig> findAll() {
        return this.selectList(null);
    }

    default Optional<FormConfig> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from form_config formConfig where formConfig.business_type = #{businessTypeId}")
    void deleteAllByBusinessTypeId(@Param("businessTypeId") Long businessTypeId);

    default FormConfig saveAndGet(FormConfig formConfig) {
        if (Objects.nonNull(formConfig.getId())) {
            this.updateById(formConfig);
        } else {
            this.insert(formConfig);
        }
        return this.selectById(formConfig.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
