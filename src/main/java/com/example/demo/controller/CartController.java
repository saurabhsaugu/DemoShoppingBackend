package com.example.demo.controller;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.CartResponseDTO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/{username}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable String username) {
        Cart cart = cartService.getCartByUsername(username);
        List<CartResponseDTO.CartItemDTO> cartItems = new java.util.ArrayList<>();
        CartResponseDTO dto;
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            // No cart or no items: return 200 OK with empty cart and message
            dto = new CartResponseDTO(null, null, cartItems);
            dto.message = "No product in cart";
            return ResponseEntity.ok(dto);
        }
        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            cartItems.add(new CartResponseDTO.CartItemDTO(
                p.getId(),
                p.getDescription(),
                p.getName(),
                p.getPrice(),
                item.getQuantity()
            ));
        }
        dto = new CartResponseDTO(cart.getId(), cart.getUser().getId(), cartItems);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/add")
    public ResponseEntity<Cart> addToCartGet(@RequestParam(required = false) String username, @RequestParam(required = false) Long productId, @RequestParam(required = false) Integer quantity) {
        logger.info("addToCart (GET) called with username={}, productId={}, quantity={}", username, productId, quantity);
        if (username == null || productId == null || quantity == null) {
            logger.error("Missing required parameters: username={}, productId={}, quantity={}", username, productId, quantity);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cartService.addProductToCart(username, productId, quantity));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCartPost(@RequestBody AddToCartRequest req) {
        logger.info("addToCart (POST) called with username={}, productId={}, quantity={}", req.username, req.productId, req.quantity);
        if (req.username == null || req.productId == null || req.quantity == null) {
            logger.error("Missing required parameters: username={}, productId={}, quantity={}", req.username, req.productId, req.quantity);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cartService.addProductToCart(req.username, req.productId, req.quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items/{cartId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCartItems(cartId));
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{cartId}/{productId}")
    public ResponseEntity<Void> removeFromCartByCartIdAndProductId(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.removeProductFromCartByCartIdAndProductId(cartId, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-quantity/{cartId}/{productId}/{newQuantity}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable int newQuantity) {
        cartService.updateCartItemQuantity(cartId, productId, newQuantity);
        return ResponseEntity.ok().build();
    }
}
