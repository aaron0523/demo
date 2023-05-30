package com.demo.repository.jpa.order;

import com.demo.domain.member.Member;
import com.demo.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);
}
