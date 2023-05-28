package com.demo.service.item;

import com.demo.domain.item.Item;
import com.demo.domain.item.ItemType;
import com.demo.repository.support.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Transactional
    public void addItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            itemRepository.delete(item.get());
        }
    }

    public Page<Item> getPaginatedItems(Pageable pageable) {
        return itemRepository.getPaginatedItems(pageable);
    }
}