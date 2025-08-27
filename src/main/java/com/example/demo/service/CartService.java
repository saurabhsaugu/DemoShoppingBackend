package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart getCartByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) return null;
        return getCartByUserId(userOpt.get().getId());
    }

    @Transactional
    public Cart addProductToCart(String username, Long productId, int quantity) {
        logger.info("Adding product to cart: username={}, productId={}, quantity={}", username, productId, quantity);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            logger.error("User not found: {}", username);
            throw new IllegalArgumentException("User not found: " + username);
        }
        User user = userOpt.get();
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            logger.info("No cart found for user, creating new cart");
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new java.util.ArrayList<>());
            cart = cartRepository.save(cart);
        } else if (cart.getItems() == null) {
            logger.info("Cart items list is null, initializing");
            cart.setItems(new java.util.ArrayList<>());
        }
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            logger.error("Product not found: {}", productId);
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        Product product = productOpt.get();
        // Check if CartItem for this product already exists
        CartItem existingItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                existingItem = item;
                break;
            }
        }
        if (existingItem != null) {
            // Increase quantity
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
            logger.info("Updated quantity for existing CartItem id={}, new quantity={}", existingItem.getId(), existingItem.getQuantity());
        } else {
            // Create new CartItem
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItemRepository.save(item);
            cart.getItems().add(item);
            logger.info("CartItem created with id={}, productId={}, quantity={}", item.getId(), productId, quantity);
        }
        cart = cartRepository.save(cart);
        logger.info("Cart saved with id={}, total items={}", cart.getId(), cart.getItems().size());
        return cartRepository.findByUserId(user.getId());
    }

    public void removeProductFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public List<CartItem> getCartItems(Long cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        return cart.map(Cart::getItems).orElse(null);
    }

    @Transactional
    public void deleteCart(Long cartId) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            if (cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    cartItemRepository.delete(item);
                }
            }
            cartRepository.delete(cart);
        }
    }

    @Transactional
    public void removeProductFromCartByCartIdAndProductId(Long cartId, Long productId) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            CartItem toRemove = null;
            if (cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    if (item.getProduct().getId().equals(productId)) {
                        toRemove = item;
                        break;
                    }
                }
            }
            if (toRemove != null) {
                cartItemRepository.delete(toRemove);
                cart.getItems().remove(toRemove); // Remove from cart's items list
                cartRepository.save(cart); // Save cart to update its state
                // Optionally, delete cart if empty
                if (cart.getItems().isEmpty()) {
                    cartRepository.delete(cart);
                }
            }
        }
    }

    @Transactional
    public void updateCartItemQuantity(Long cartId, Long productId, int newQuantity) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            if (cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    if (item.getProduct().getId().equals(productId)) {
                        if (newQuantity < 1) {
                            cartItemRepository.delete(item);
                        } else {
                            item.setQuantity(newQuantity);
                            cartItemRepository.save(item);
                        }
                        break;
                    }
                }
            }
        }
    }
}
