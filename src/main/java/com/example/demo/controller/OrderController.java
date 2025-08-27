package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.OrderHistoryResponseDTO;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestParam String username) {
        Order order = orderService.placeOrder(username);
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<OrderHistoryResponseDTO> getOrderHistory(@PathVariable String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            // User not found, return empty order history
            return ResponseEntity.ok(new OrderHistoryResponseDTO(null, new java.util.ArrayList<>()));
        }
        User user = userOpt.get();
        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        List<OrderHistoryResponseDTO.OrderDTO> orderDTOs = new java.util.ArrayList<>();
        for (Order order : orders) {
            List<OrderHistoryResponseDTO.OrderItemDTO> itemDTOs = new java.util.ArrayList<>();
            if (order.getItems() != null) {
                for (var item : order.getItems()) {
                    var p = item.getProduct();
                    itemDTOs.add(new OrderHistoryResponseDTO.OrderItemDTO(
                        p.getId(),
                        p.getDescription(),
                        p.getName(),
                        p.getPrice(),
                        item.getQuantity()
                    ));
                }
            }
            orderDTOs.add(new OrderHistoryResponseDTO.OrderDTO(order.getId(), itemDTOs));
        }
        return ResponseEntity.ok(new OrderHistoryResponseDTO(user.getId(), orderDTOs));
    }
}
