package com.begcode.monolith.repository.base;

import com.begcode.monolith.domain.UploadImage;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the UploadImage entity.
 */
@NoRepositoryBean
public interface UploadImageBaseRepository<E extends UploadImage> extends BaseCrudMapper<UploadImage> {
    default Optional<UploadImage> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(uploadImage -> {
            Binder.bindRelations(uploadImage, new String[] { "category" });
            return uploadImage;
        });
    }

    default List<UploadImage> findAll() {
        return this.selectList(null);
    }

    default Optional<UploadImage> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from upload_image uploadImage where uploadImage.category = #{categoryId}")
    void deleteAllByCategoryId(@Param("categoryId") Long categoryId);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
