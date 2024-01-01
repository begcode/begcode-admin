package com.begcode.monolith.repository.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.begcode.monolith.domain.UReportFile;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the UReportFile entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface UReportFileBaseRepository<E extends UReportFile> extends BaseCrudMapper<UReportFile> {
    default List<UReportFile> findAll() {
        return this.selectList(null);
    }

    default Optional<UReportFile> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default Boolean existsByName(String name) {
        return this.selectCount(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name)) > 0;
    }

    default Optional<UReportFile> getByName(String name) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name)));
    }

    default void deleteByName(String name) {
        this.delete(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
