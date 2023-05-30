package com.demo.service.order;

import com.demo.domain.delivery.Delivery;
import com.demo.domain.delivery.DeliveryStatus;
import com.demo.domain.item.Item;
import com.demo.domain.item.ItemType;
import com.demo.domain.member.Member;
import com.demo.domain.order.Order;
import com.demo.domain.order.OrderStatus;
import com.demo.domain.orderItem.OrderItem;
import com.demo.repository.support.ItemRepository;
import com.demo.repository.support.MemberRepository;
import com.demo.repository.support.OrderRepository;
import com.demo.web.dto.order.OrderSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public void saveOrder(Long memberId, Long itemId, int count) {
        try {
        // 엔티티 조회 -> 트랜잭션 안에서 엔티티를 조회해야 영속 상태가 유지된다.
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        Member member = memberOptional.get();
        Item item = itemOptional.get();

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.READY);
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        } catch (Exception e) {
            log.error("주문 저장 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("주문 저장 중 오류 발생");
        }
    }

    /**
     * 주문 내역 조회
     */
    public List<Order> getOrdersByStatusAndType(String status, String type) {
        if (status == null && type == null) {
            return orderRepository.findAll();
        } else if (status != null && type == null) {
            return orderRepository.findByStatus(OrderStatus.valueOf(status));
        } else if (status == null) {
            return orderRepository.findByItemType(ItemType.valueOf(type));
        } else {
            return orderRepository.findByStatusAndItemType(OrderStatus.valueOf(status), ItemType.valueOf(type));
        }
    }

    public List<Order> getOrdersBySearchDto(OrderSearchDto orderSearchDto) {
        return orderRepository.findBySearchDto(orderSearchDto);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId);
            order.cancel();
        } catch (Exception e) {
            log.error("주문 취소 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("주문 취소 중 오류 발생");
        }
    }

}
