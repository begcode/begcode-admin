package com.begcode.monolith.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.function.Consumer;

public class CriteriaUtil {

    public static <ENTITY> QueryWrapper<ENTITY> build(
        Boolean useOr,
        QueryWrapper<ENTITY> queryWrapper,
        Consumer<QueryWrapper<ENTITY>> consumer
    ) {
        if (useOr) {
            return queryWrapper.or(true, consumer);
        } else {
            return queryWrapper.and(true, consumer);
        }
    }
}
