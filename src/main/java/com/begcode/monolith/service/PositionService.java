package com.begcode.monolith.service;

import com.begcode.monolith.domain.Position;
import com.begcode.monolith.repository.PositionRepository;
import com.begcode.monolith.service.base.PositionBaseService;
import com.begcode.monolith.service.mapper.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Position}.
 */
@Service
public class PositionService extends PositionBaseService<PositionRepository, Position> {

    private final Logger log = LoggerFactory.getLogger(PositionService.class);

    public PositionService(PositionRepository positionRepository, CacheManager cacheManager, PositionMapper positionMapper) {
        super(positionRepository, cacheManager, positionMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
