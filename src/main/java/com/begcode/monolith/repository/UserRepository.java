package com.begcode.monolith.repository;

import com.begcode.monolith.domain.User;
import com.begcode.monolith.repository.base.UserBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository extends UserBaseRepository<User> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
