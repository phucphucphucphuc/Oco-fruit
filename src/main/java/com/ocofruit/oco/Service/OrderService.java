package com.ocofruit.oco.Service;

import com.ocofruit.oco.Model.Order;
import com.ocofruit.oco.Model.OrderItem;
import com.ocofruit.oco.Model.Product;
import com.ocofruit.oco.Repository.OrderRepository;
import com.ocofruit.oco.Repository.ProductRepository;
<<<<<<< HEAD
import com.ocofruit.oco.Repository.UserRepository;

=======
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(String customerName, String customerPhone,
                              String address, List<Long> fruitIds) {

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setAddress(address);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (Long productId : fruitIds) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + productId));

            OrderItem item = new OrderItem(savedOrder, product, 1, product.getPrice());
            items.add(item);
            total += product.getPrice();
        }

        savedOrder.setItems(items);
        savedOrder.setTotalPrice(total);

        return orderRepository.save(savedOrder);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID: " + id));
    }
<<<<<<< HEAD
    @Autowired
private UserRepository userRepository;

@Transactional
public Order createOrder(String customerName, String customerPhone,
                          String address, List<Long> fruitIds, String username) {
    Order order = new Order();
    order.setCustomerName(customerName);
    order.setCustomerPhone(customerPhone);
    order.setAddress(address);

    // Gắn user vào order
    userRepository.findByUsername(username).ifPresent(order::setUser);

    Order savedOrder = orderRepository.save(order);

    List<OrderItem> items = new ArrayList<>();
    double total = 0;
    for (Long productId : fruitIds) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        OrderItem item = new OrderItem(savedOrder, product, 1, product.getPrice());
        items.add(item);
        total += product.getPrice();
    }
    savedOrder.setItems(items);
    savedOrder.setTotalPrice(total);
    return orderRepository.save(savedOrder);
}
=======
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
}