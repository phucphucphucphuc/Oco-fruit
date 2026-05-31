package com.ocofruit.oco.Repository;

import com.ocofruit.oco.Model.Order;
<<<<<<< HEAD
import com.ocofruit.oco.Model.User;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
>>>>>>> 130961b1d5aec426173659935509f03071d3702f

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerPhone(String phone);
<<<<<<< HEAD
    List<Order> findByOrderByOrderDateDesc();
    List<Order> findByUserOrderByOrderDateDesc(User user);
    long countByStatus(String status);
    List<Order> findByUser(User user);

@Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'DELIVERED'")
double sumRevenue();

List<Order> findTop10ByOrderByOrderDateDesc();
    
}
=======
    List<Order> findByOrderByOrderDateDesc(); // Mới nhất trước
}
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
