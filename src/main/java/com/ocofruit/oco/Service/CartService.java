package com.ocofruit.oco.Service;

import com.ocofruit.oco.Model.CartItem;
import com.ocofruit.oco.Model.Product;
import com.ocofruit.oco.Repository.CartRepository;
import com.ocofruit.oco.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void addToCart(String username, List<Long> fruitIds) {
        for (Long productId : fruitIds) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
            cartRepository.save(new CartItem(username, product));
        }
    }

    public List<CartItem> getCartItems(String username) {
        return cartRepository.findByUsername(username);
    }

    public List<List<CartItem>> getCartBoxes(String username) {
        List<CartItem> items = cartRepository.findByUsername(username);
        List<List<CartItem>> boxes = new ArrayList<>();
        for (CartItem item : items) {
            boxes.add(Collections.singletonList(item));
        }
        return boxes;
    }

    public long countBoxes(String username) {
        return cartRepository.countByUsername(username);
    }

    @Transactional
    public void removeItem(Long itemId) {
        cartRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCart(String username) {
        cartRepository.deleteByUsername(username);
    }

    public double getTotalPrice(String username) {
        return cartRepository.findByUsername(username)
            .stream()
            .mapToDouble(item -> item.getProduct().getPrice())
            .sum();
    }
}