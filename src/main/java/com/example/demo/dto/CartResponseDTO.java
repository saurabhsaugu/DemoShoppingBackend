package com.example.demo.dto;

import java.util.List;

public class CartResponseDTO {
    public Long cartId;
    public Long userID;
    public List<CartItemDTO> cartItems;
    public String message;

    public CartResponseDTO(Long cartId, Long userID, List<CartItemDTO> cartItems) {
        this.cartId = cartId;
        this.userID = userID;
        this.cartItems = cartItems;
        this.message = null;
    }

    public static class CartItemDTO {
        public Long productID;
        public String description;
        public String name;
        public Double price;
        public Integer quantity;

        public CartItemDTO(Long productID, String description, String name, Double price, Integer quantity) {
            this.productID = productID;
            this.description = description;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
