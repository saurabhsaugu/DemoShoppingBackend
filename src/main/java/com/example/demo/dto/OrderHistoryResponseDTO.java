package com.example.demo.dto;

import java.util.List;

public class OrderHistoryResponseDTO {
    public Long userID;
    public List<OrderDTO> orders;

    public OrderHistoryResponseDTO(Long userID, List<OrderDTO> orders) {
        this.userID = userID;
        this.orders = orders;
    }

    public static class OrderDTO {
        public Long orderId;
        public List<OrderItemDTO> orderItems;

        public OrderDTO(Long orderId, List<OrderItemDTO> orderItems) {
            this.orderId = orderId;
            this.orderItems = orderItems;
        }
    }

    public static class OrderItemDTO {
        public Long productID;
        public String description;
        public String name;
        public Double price;
        public Integer quantity;

        public OrderItemDTO(Long productID, String description, String name, Double price, Integer quantity) {
            this.productID = productID;
            this.description = description;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}

