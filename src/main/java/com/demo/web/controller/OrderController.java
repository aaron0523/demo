package com.demo.web.controller;

import com.demo.domain.delivery.Delivery;
import com.demo.domain.delivery.DeliveryStatus;
import com.demo.domain.item.Item;
import com.demo.domain.member.Member;
import com.demo.domain.order.Order;
import com.demo.domain.orderItem.OrderItem;
import com.demo.service.item.ItemService;
import com.demo.service.member.MemberService;
import com.demo.service.order.OrderService;
import com.demo.web.SessionConst;
import com.demo.web.dto.order.OrderDto;
import com.demo.web.dto.order.OrderSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;

    // 주문 페이지
    @GetMapping()
    public String createForm(Model model) {
        List<Item> items = itemService.findAllItems();
        model.addAttribute("items", items);
        model.addAttribute("orderDto", new OrderDto());
        return "order/order";
    }

    // 주문 처리
    @PostMapping()
    public String placeOrder(@ModelAttribute("orderDto") OrderDto orderDto,
                             @RequestParam("itemId") Long itemId,
                             @RequestParam("count") int count,
                             HttpSession session) {

        // 회원 정보 가져오기
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        try {
        // 주문 저장 처리
        orderService.saveOrder(loggedInMember.getId(), itemId, count);

        return "redirect:/";
        } catch (Exception e) {
            // 예외 처리 로직 작성
            log.error("주문 처리 중 오류 발생: {}", e.getMessage());
            // 예외에 따라 적절한 에러 페이지나 메시지를 반환하도록 수정하시기 바랍니다.
            return "error"; // 예시로 에러 페이지를 "error"로 설정
        }
    }

    // 주문 내역 페이지
    @GetMapping("/list")
    public String orderList(Model model,
                            @ModelAttribute("orderSearch") OrderSearchDto orderSearchDto,
                            HttpSession session) {

        // 사용자 정보 가져오기
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        String nickname = loggedInMember.getNickName();

        // 주문 내역 조회
        List<Order> orders = orderService.getOrdersBySearchDto(orderSearchDto);

        if (orders == null) {
            log.error("주문 내역이 없습니다.");
            return "error"; // 예시로 에러 페이지를 "error"로 설정
        }

        model.addAttribute("orders", orders);
        model.addAttribute("nickname", nickname);

        return "order/orderList";
    }

    // 주문 취소 처리
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@RequestParam("orderId") Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return "redirect:/orders/list";
        } catch (Exception e) {
            log.error("주문 취소 중 오류 발생: {}", e.getMessage());
            return "error";
        }
    }


}
