package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Order placeOrder(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        User user = userOpt.get();
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return null;
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setItems(new java.util.ArrayList<>());
        order = orderRepository.save(order);
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);
            order.getItems().add(orderItem);
        }
        // Clear cart items and delete cart with logging and error handling
        cart.getItems().clear();
        cartRepository.save(cart);
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderService.class);
        try {
            logger.info("Attempting to break user-cart reference for user: {} (cartId: {})", user.getUsername(), cart.getId());
            user.setCart(null);
            userRepository.save(user);
            logger.info("User-cart reference broken for user: {} (cartId: {})", user.getUsername(), cart.getId());
            logger.info("Attempting to delete cart for user: {} (cartId: {})", user.getUsername(), cart.getId());
            cartRepository.delete(cart);
            logger.info("Cart deleted for user: {} (cartId: {})", user.getUsername(), cart.getId());
        } catch (Exception e) {
            logger.error("Failed to delete cart for user: {} (cartId: {}) - Exception: {}", user.getUsername(), cart.getId(), e.getMessage(), e);
        }
        order = orderRepository.save(order);
        return orderRepository.findById(order.getId()).orElse(null);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
