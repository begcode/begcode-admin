package com.begcode.monolith.repository;

import com.begcode.monolith.domain.Position;
import com.begcode.monolith.repository.base.PositionBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PositionRepository extends PositionBaseRepository<Position> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
