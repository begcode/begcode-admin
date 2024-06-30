package com.begcode.monolith.settings.repository.base;

import com.begcode.monolith.settings.domain.CommonFieldData;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the CommonFieldData entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface CommonFieldDataBaseRepository<E extends CommonFieldData> extends BaseCrudMapper<CommonFieldData> {
    default List<CommonFieldData> findAll() {
        return this.selectList(null);
    }

    default Optional<CommonFieldData> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from common_field_data commonFieldData where commonFieldData.site_config = #{siteConfigId}")
    void deleteAllBySiteConfigId(@Param("siteConfigId") Long siteConfigId);

    @Select("delete from common_field_data commonFieldData where commonFieldData.dictionary = #{dictionaryId}")
    void deleteAllByDictionaryId(@Param("dictionaryId") Long dictionaryId);

    default CommonFieldData saveAndGet(CommonFieldData commonFieldData) {
        if (Objects.nonNull(commonFieldData.getId())) {
            this.updateById(commonFieldData);
        } else {
            this.insert(commonFieldData);
        }
        return this.selectById(commonFieldData.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
