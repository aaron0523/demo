package com.demo.repository.support;

import com.demo.domain.item.ItemType;
import com.demo.domain.order.Order;
import com.demo.domain.order.OrderStatus;
import com.demo.web.dto.order.OrderSearchDto;

import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<Order> findBySearchDto(OrderSearchDto orderSearchDto);
    Order findById(Long id);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByItemType(ItemType type);
    List<Order> findByStatusAndItemType(OrderStatus status, ItemType type);
    List<Order> findAll();
}
