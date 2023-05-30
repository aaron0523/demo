package com.demo.service.order;

import com.demo.domain.order.Order;
import com.demo.web.dto.order.OrderSearchDto;

import java.util.List;

public interface OrderService {
    void saveOrder(Long memberId, Long itemId, int count);
    List<Order> getOrdersBySearchDto(OrderSearchDto orderSearchDto);
    void cancelOrder(Long orderId);
}
