package com.demo.service.item;


import com.demo.domain.item.Item;
import com.demo.domain.item.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getAllItems();
    Optional<Item> getItemById(Long id);
    void addItem(Item item);
    void updateItem(Item item);
    void deleteItem(Long id);
    Page<Item> getPaginatedItems(Pageable pageable);
}