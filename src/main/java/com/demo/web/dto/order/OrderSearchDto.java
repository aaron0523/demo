package com.demo.web.dto.order;

import lombok.Data;

@Data
public class OrderSearchDto {

    private String memberName;
    private String status;
    private String type;

}
