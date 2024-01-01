package com.begcode.monolith.repository.base;

import com.begcode.monolith.domain.UploadFile;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the UploadFile entity.
 */
@NoRepositoryBean
public interface UploadFileBaseRepository<E extends UploadFile> extends BaseCrudMapper<UploadFile> {
    default Optional<UploadFile> findOneWithEagerRelationships(Long id) {
        return Optional
            .ofNullable(this.selectById(id))
            .map(uploadFile -> {
                Binder.bindRelations(uploadFile, new String[] { "category" });
                return uploadFile;
            });
    }

    default List<UploadFile> findAll() {
        return this.selectList(null);
    }

    default Optional<UploadFile> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from upload_file uploadFile where uploadFile.category = #{categoryId}")
    void deleteAllByCategoryId(@Param("categoryId") Long categoryId);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
