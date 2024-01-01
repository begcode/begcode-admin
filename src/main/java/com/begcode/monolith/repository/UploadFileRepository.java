package com.begcode.monolith.repository;

import com.begcode.monolith.domain.UploadFile;
import com.begcode.monolith.repository.base.UploadFileBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadFileRepository extends UploadFileBaseRepository<UploadFile> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
