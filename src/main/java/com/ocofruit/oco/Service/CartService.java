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

    // Nhóm từng 3 item thành 1 hộp
    public List<List<CartItem>> getCartBoxes(String username) {
        List<CartItem> items = cartRepository.findByUsername(username);
        List<List<CartItem>> boxes = new ArrayList<>();

        for (int i = 0; i < items.size(); i += 3) {
            boxes.add(items.subList(i, Math.min(i + 3, items.size())));
        }
        return boxes;
    }
            // Đếm hộp đầy đủ (đủ 3 trái)
        public long countCompleteBoxes(String username) {
            long total = cartRepository.countByUsername(username);
            return total / 3;
        }

        // Kiểm tra có hộp chưa đủ không
        public boolean hasIncompleteBox(String username) {
            long total = cartRepository.countByUsername(username);
            return total % 3 != 0;
        }

    public long countBoxes(String username) {
        long total = cartRepository.countByUsername(username);
        return (long) Math.ceil((double) total / 3);
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