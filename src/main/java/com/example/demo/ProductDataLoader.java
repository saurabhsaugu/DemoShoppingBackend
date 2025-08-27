package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    public ProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("Apples", "Fresh red apples", 2.99, 100));
            productRepository.save(new Product("Bananas", "Organic bananas", 1.49, 120));
            productRepository.save(new Product("Milk", "1L whole milk", 0.99, 80));
            productRepository.save(new Product("Bread", "Whole wheat bread loaf", 1.99, 60));
            productRepository.save(new Product("Eggs", "Pack of 12 eggs", 2.49, 90));
            productRepository.save(new Product("Tomatoes", "Vine-ripened tomatoes", 2.29, 70));
            productRepository.save(new Product("Potatoes", "5lb bag of potatoes", 3.49, 50));
            productRepository.save(new Product("Carrots", "Fresh carrots, 1lb", 1.29, 75));
            productRepository.save(new Product("Rice", "2kg basmati rice", 4.99, 40));
            productRepository.save(new Product("Chicken Breast", "Boneless chicken breast, 1lb", 5.99, 30));
        }
    }
}
