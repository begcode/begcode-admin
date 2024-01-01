package com.begcode.monolith.oss.repository;

import com.begcode.monolith.oss.domain.OssConfig;
import com.begcode.monolith.oss.repository.base.OssConfigBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OssConfigRepository extends OssConfigBaseRepository<OssConfig> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
