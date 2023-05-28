package com.demo.repository.support;

import com.demo.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    void save(Item item);
    Optional<Item> findById(Long id);
    List<Item> findAll();
    void delete(Item item);
    Page<Item> getPaginatedItems(Pageable pageable);
}
