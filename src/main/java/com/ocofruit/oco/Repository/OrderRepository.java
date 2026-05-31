package com.ocofruit.oco.Repository;

import com.ocofruit.oco.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerPhone(String phone);
    List<Order> findByOrderByOrderDateDesc(); // Mới nhất trước
}