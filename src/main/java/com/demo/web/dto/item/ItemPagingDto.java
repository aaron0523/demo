package com.demo.web.dto.item;

import com.demo.domain.item.Item;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ItemPagingDto {
    private Page<Item> itemPage;
}
