package com.demo.repository.jpa.order;

import com.demo.domain.delivery.DeliveryStatus;
import com.demo.domain.item.ItemType;
import com.demo.domain.member.Member;
import com.demo.domain.order.Order;
import com.demo.domain.order.OrderStatus;
import com.demo.repository.support.OrderRepository;
import com.demo.repository.support.QuerydslRepositorySupport;
import com.demo.web.dto.order.OrderSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.demo.domain.order.QOrder.order;
import static com.demo.domain.member.QMember.member;
import static com.demo.domain.orderItem.QOrderItem.orderItem;

@Repository
public class JpaOrderRepository extends QuerydslRepositorySupport implements OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public JpaOrderRepository(EntityManager em) {
        super(Order.class);
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findByMember(Member memberParam) {
        return queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .where(order.member.eq(memberParam))
                .fetch();
    }

    @Override
    public void save(Order order) {
        em.persist(order);
    }

    @Override
    public List<Order> findBySearchDto(OrderSearchDto orderSearchDto) {

        BooleanBuilder whereBuilder = new BooleanBuilder();

        if (StringUtils.hasText(orderSearchDto.getMemberName())) {
            whereBuilder.and(member.name.containsIgnoreCase(orderSearchDto.getMemberName()));
        }

        if (StringUtils.hasText(orderSearchDto.getStatus())) {
            whereBuilder.and(order.delivery.status.eq(DeliveryStatus.valueOf(orderSearchDto.getStatus())));
        }

        if (StringUtils.hasText(orderSearchDto.getType())) {
            whereBuilder.and(order.orderItems.any().item.type.eq(ItemType.valueOf(orderSearchDto.getType())));
        }

        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .where(whereBuilder)
                .fetch();
    }


    @Override
    public List<Order> findAll() {
        return queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .fetch();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .where(order.status.eq(status))
                .fetch();
    }

    @Override
    public List<Order> findByItemType(ItemType type) {
        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .where(order.orderItems.any().item.type.eq(type))
                .fetch();
    }

    @Override
    public List<Order> findByStatusAndItemType(OrderStatus status, ItemType type) {
        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .where(
                        order.status.eq(status),
                        order.orderItems.any().item.type.eq(type)
                )
                .fetch();
    }
}
