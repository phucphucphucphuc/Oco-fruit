package com.ocofruit.oco.Repository;

import com.ocofruit.oco.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUsername(String username);
    long countByUsername(String username);
    void deleteByUsername(String username);
}