package com.demo.web.controller;

import com.demo.domain.item.Item;
import com.demo.domain.item.ItemType;
import com.demo.service.item.ItemService;
import com.demo.web.dto.item.ItemForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final int PAGE_SIZE = 10;
    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ItemForm());
        return "item/createItemForm";
    }

    @PostMapping("/new")
    public String create(ItemForm form) {
        Item item = new Item();
        item.updateItem(form.getName(), form.getPrice(), form.getStockQuantity(), ItemType.valueOf(form.getType()));
        itemService.addItem(item);
        return "redirect:/items";
    }

    /**
     * 상품 목록
     */
    @GetMapping()
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<Item> itemPage = itemService.getPaginatedItems(pageable);

        List<Item> items = itemService.getAllItems();

        model.addAttribute("itemPage", itemPage);
        model.addAttribute("items", items);
        return "item/itemList";
    }

    /**
     * 상품 수정 폼
     */
    @GetMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.getItemById(itemId).get();
        ItemForm form = new ItemForm();
        form.updateItemForm(item.getId(), item.getName(), item.getPrice(), item.getStockQuantity(), item.getType().name());
        model.addAttribute("form", form);
        return "item/editItemForm";
    }

    /**
     * 상품 수정
     */
    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") ItemForm form) {
        Optional<Item> optionalItem = itemService.getItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.updateItem(form.getName(), form.getPrice(), form.getStockQuantity(), ItemType.valueOf(form.getType()));
            itemService.updateItem(item);
        } else {
            // 예외 처리 또는 알림 등을 수행
            return "item/editItemForm";
        }
        return "redirect:/items";
    }

    @GetMapping("/{itemId}/delete")
    public String deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return "redirect:/items";
    }
}