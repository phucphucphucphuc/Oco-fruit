package com.ocofruit.oco.Repository;

import com.ocofruit.oco.Model.Order;

import com.ocofruit.oco.Model.User;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerPhone(String phone);

    List<Order> findByOrderByOrderDateDesc();
    List<Order> findByUserOrderByOrderDateDesc(User user);
    long countByStatus(String status);
    List<Order> findByUser(User user);

@Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'DELIVERED'")
double sumRevenue();

List<Order> findTop10ByOrderByOrderDateDesc();
    
}

    List<Order> findByOrderByOrderDateDesc();
}