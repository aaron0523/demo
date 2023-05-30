package com.demo.domain.delivery;

import com.demo.domain.address.Address;
import com.demo.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // 타입은 반드시 String으로!
    private DeliveryStatus status; //READY, COMP
}
