package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Model.Category;
import com.ocofruit.oco.Model.Order;
import com.ocofruit.oco.Model.User;
import com.ocofruit.oco.Repository.CategoryRepository;
import com.ocofruit.oco.Repository.OrderRepository;
import com.ocofruit.oco.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private CategoryRepository categoryRepository;

    // ===== DASHBOARD =====
    @GetMapping
    public String dashboard(Model model) {
        long totalUsers    = userRepository.count();
        long totalOrders   = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus("PENDING");
        double totalRevenue = orderRepository.sumRevenue();

        model.addAttribute("totalUsers",    totalUsers);
        model.addAttribute("totalOrders",   totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("totalRevenue",  totalRevenue);
        model.addAttribute("recentOrders",  orderRepository.findTop10ByOrderByOrderDateDesc());
        return "admin/dashboard";
    }

    // ===== USERS =====
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
        ra.addFlashAttribute("success", "Cập nhật trạng thái user thành công!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String role,
                             RedirectAttributes ra) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
        ra.addFlashAttribute("success", "Đổi role thành công!");
        return "redirect:/admin/users";
    }

    // ===== ORDERS =====
    @GetMapping("/orders")
    public String listOrders(@RequestParam(required = false) String status, Model model) {
        List<Order> orders = (status != null && !status.isEmpty())
                ? orderRepository.findByStatus(status)
                : orderRepository.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status);
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam String status,
                                    RedirectAttributes ra) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
        ra.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công!");
        return "redirect:/admin/orders";
    }

    // ===== CATEGORIES =====
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam String description,
                              RedirectAttributes ra) {
        Category cat = new Category();
        cat.setName(name);
        cat.setDescription(description);
        categoryRepository.save(cat);
        ra.addFlashAttribute("success", "Thêm category thành công!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes ra) {
        categoryRepository.deleteById(id);
        ra.addFlashAttribute("success", "Xóa category thành công!");
        return "redirect:/admin/categories";
    }
    @GetMapping("/gen-hash")
@ResponseBody
public String genHash() {
    return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin123");
}
}