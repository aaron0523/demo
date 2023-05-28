package com.demo.repository.jpa.item;

import com.demo.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAll(Pageable pageable);
}
