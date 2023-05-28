package com.demo.web.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemForm {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String type;
    private String author;
    private String isbn;
    private String category;
    private String style;

    public void updateItemForm (Long id, String name, int price, int stockQuantity, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.type = type;
    }
}