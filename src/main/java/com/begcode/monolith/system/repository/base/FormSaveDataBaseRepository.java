package com.begcode.monolith.system.repository.base;

import com.begcode.monolith.system.domain.FormSaveData;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the FormSaveData entity.
 */
@NoRepositoryBean
public interface FormSaveDataBaseRepository<E extends FormSaveData> extends BaseCrudMapper<FormSaveData> {
    default Optional<FormSaveData> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(formSaveData -> {
            Binder.bindRelations(formSaveData, new String[] { "formConfig" });
            return formSaveData;
        });
    }

    default List<FormSaveData> findAll() {
        return this.selectList(null);
    }

    default Optional<FormSaveData> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from form_save_data form_save_data where form_save_data.form_config_id = #{formConfigId}")
    void deleteAllByFormConfigId(@Param("formConfigId") Long formConfigId);

    default FormSaveData saveAndGet(FormSaveData formSaveData) {
        if (Objects.nonNull(formSaveData.getId())) {
            this.updateById(formSaveData);
        } else {
            this.insert(formSaveData);
        }
        return this.selectById(formSaveData.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
