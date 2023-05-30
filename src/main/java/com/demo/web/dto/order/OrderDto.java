package com.demo.web.dto.order;

import lombok.Data;

@Data
public class OrderDto {

    private String name;
    private int price;
    private int stockQuantity;

}
