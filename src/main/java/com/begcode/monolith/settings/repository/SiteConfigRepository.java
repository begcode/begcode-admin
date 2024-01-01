package com.begcode.monolith.settings.repository;

import com.begcode.monolith.settings.domain.SiteConfig;
import com.begcode.monolith.settings.repository.base.SiteConfigBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SiteConfigRepository extends SiteConfigBaseRepository<SiteConfig> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
