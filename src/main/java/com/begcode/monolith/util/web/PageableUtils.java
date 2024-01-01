package com.begcode.monolith.util.web;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

public class PageableUtils {

    public static <T> Page<T> toPage(Pageable pageable) {
        Page<T> result = new Page<T>().setCurrent(pageable.getPageNumber() + 1).setSize(pageable.getPageSize());
        List<OrderItem> orderItems = pageable
            .getSort()
            .stream()
            .map(order -> {
                OrderItem item = new OrderItem();
                item.setAsc(order.isAscending());
                item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, order.getProperty()));
                return item;
            })
            .collect(Collectors.toList());
        result.setOrders(orderItems);
        return result;
    }
}
