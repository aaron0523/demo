package com.demo.service.item;


import com.demo.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAllItems();
    Optional<Item> findById(Long id);
    void addItem(Item item);
    void updateItem(Item item);
    void deleteItem(Long id);
    Page<Item> getPaginatedItems(Pageable pageable);
}