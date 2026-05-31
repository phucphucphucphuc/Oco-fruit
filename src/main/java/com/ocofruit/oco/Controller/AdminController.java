package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Model.Category;
import com.ocofruit.oco.Model.Order;
import com.ocofruit.oco.Model.Product;
import com.ocofruit.oco.Model.User;
import com.ocofruit.oco.Repository.CategoryRepository;
import com.ocofruit.oco.Repository.OrderRepository;
import com.ocofruit.oco.Repository.ProductRepository;
import com.ocofruit.oco.Repository.UserRepository;
import com.ocofruit.oco.Service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;



@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    

    // ===== DASHBOARD =====
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("pendingOrders", orderRepository.countByStatus("PENDING"));
        double totalRevenue = orderRepository.findAll().stream()
            .filter(o -> o.getTotalPrice() != null)
            .mapToDouble(Order::getTotalPrice).sum();
        model.addAttribute("totalRevenue", (long) totalRevenue);

        model.addAttribute("recentOrders",
            orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderDate,
                    Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10).collect(Collectors.toList()));

        List<String> last7Days = new ArrayList<>();
        List<Long> revenueByDay = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dayStart = LocalDateTime.now().minusDays(i).toLocalDate().atStartOfDay();
            LocalDateTime dayEnd = dayStart.plusDays(1);
            last7Days.add(dayStart.format(fmt));
            double dayRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getOrderDate() != null
                    && o.getOrderDate().isAfter(dayStart)
                    && o.getOrderDate().isBefore(dayEnd)
                    && o.getTotalPrice() != null)
                .mapToDouble(Order::getTotalPrice).sum();
            revenueByDay.add((long) dayRevenue);
        }
        model.addAttribute("chartDays", last7Days);
        model.addAttribute("chartRevenue", revenueByDay);

        Map<String, Long> statusMap = new LinkedHashMap<>();
        statusMap.put("PENDING",   orderRepository.countByStatus("PENDING"));
        statusMap.put("CONFIRMED", orderRepository.countByStatus("CONFIRMED"));
        statusMap.put("DELIVERED", orderRepository.countByStatus("DELIVERED"));
        statusMap.put("CANCELLED", orderRepository.countByStatus("CANCELLED"));
        model.addAttribute("statusLabels", new ArrayList<>(statusMap.keySet()));
        model.addAttribute("statusCounts", new ArrayList<>(statusMap.values()));

        Map<String, Long> productSales = new LinkedHashMap<>();
        orderRepository.findAll().forEach(order -> {
            if (order.getItems() != null) {
                order.getItems().forEach(item -> {
                    String name = item.getProduct().getName();
                    productSales.merge(name, 1L, Long::sum);
                });
            }
        });
        List<Map.Entry<String, Long>> top5 = productSales.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5).collect(Collectors.toList());
        model.addAttribute("topProductNames",  top5.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
        model.addAttribute("topProductCounts", top5.stream().map(Map.Entry::getValue).collect(Collectors.toList()));

        return "admin/dashboard";
    }

    // ===== USERS =====
    @GetMapping("/users")
public String users(Model model) {
    List<User> allUsers = userRepository.findAll();

    // Tách 2 nhóm
    List<User> staffUsers = allUsers.stream()
        .filter(u -> u.getRole().equals("ROLE_ADMIN") || u.getRole().equals("ROLE_STAFF"))
        .collect(Collectors.toList());

    List<User> regularUsers = allUsers.stream()
        .filter(u -> u.getRole().equals("ROLE_USER"))
        .collect(Collectors.toList());

    model.addAttribute("staffUsers", staffUsers);
    model.addAttribute("regularUsers", regularUsers);
    return "admin/users";
}

// Xem orders của 1 user cụ thể
@GetMapping("/users/{id}/orders")
public String userOrders(@PathVariable Long id, Model model) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found: " + id));

    List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

    model.addAttribute("targetUser", user);
    model.addAttribute("orders", orders);
    return "admin/user-orders";
}

    // Đổi role user
    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String role,
                             RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole(role);
            userRepository.save(user);
        });
        ra.addFlashAttribute("success", "Role updated successfully!");
        return "redirect:/admin/users";
    }

    // Enable/Disable user
    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(user -> {
            user.setEnabled(!user.getEnabled());
            userRepository.save(user);
        });
        ra.addFlashAttribute("success", "User status updated!");
        return "redirect:/admin/users";
    }

    // ===== ORDERS =====
    @GetMapping("/orders")
    public String orders(@RequestParam(required = false) String status, Model model) {
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderRepository.findByStatus(status);
        } else {
            orders = orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderDate,
                    Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        }
        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status);
        return "admin/orders";
    }

    // Cập nhật status đơn hàng
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam String status,
                                    RedirectAttributes ra) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
        ra.addFlashAttribute("success", "Order #" + id + " updated to " + status);
        return "redirect:/admin/orders";
    }

    // ===== CATEGORIES =====
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/categories";
    }

    // Thêm category
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              RedirectAttributes ra) {
        Category cat = new Category();
        cat.setName(name);
        cat.setDescription(description);
        categoryRepository.save(cat);
        ra.addFlashAttribute("success", "Category '" + name + "' added!");
        return "redirect:/admin/categories";
    }

    // Xóa category
    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes ra) {
        categoryRepository.deleteById(id);
        ra.addFlashAttribute("success", "Category deleted!");
        return "redirect:/admin/categories";
    }
    @Autowired private FileUploadService fileUploadService;

// ===== PRODUCTS & CATEGORIES =====
@GetMapping("/products")
public String listProducts(Model model) {
    model.addAttribute("products", productRepository.findAll());
    model.addAttribute("categories", categoryRepository.findAll());
    return "admin/products";
}

// Thêm product
@PostMapping("/products/add")
public String addProduct(
        @RequestParam String name,
        @RequestParam(required = false) String description,
        @RequestParam Double price,
        @RequestParam Integer quantity,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) MultipartFile imageFile,
        RedirectAttributes ra) {
    try {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setActive(true);

        // Set category
        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                .ifPresent(cat -> product.setCategory(cat.getName()));
        }

        // Upload image
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.saveImage(imageFile);
            product.setImageUrl(imageUrl);
        }

        productRepository.save(product);
        ra.addFlashAttribute("success", "Product '" + name + "' added successfully!");
    } catch (Exception e) {
        ra.addFlashAttribute("error", "Error: " + e.getMessage());
    }
    return "redirect:/admin/products";
}

// Toggle active/inactive product
@PostMapping("/products/{id}/toggle")
public String toggleProduct(@PathVariable Long id, RedirectAttributes ra) {
    productRepository.findById(id).ifPresent(p -> {
        p.setActive(!p.getActive());
        productRepository.save(p);
    });
    ra.addFlashAttribute("success", "Product status updated!");
    return "redirect:/admin/products";
}

// Xóa product
@PostMapping("/products/{id}/delete")
public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
    productRepository.findById(id).ifPresent(p -> {
        fileUploadService.deleteImage(p.getImageUrl());
        productRepository.deleteById(id);
    });
    ra.addFlashAttribute("success", "Product deleted!");
    return "redirect:/admin/products";
}
// Edit product
@PostMapping("/products/{id}/edit")
public String editProduct(@PathVariable Long id,
                          @RequestParam double price,
                          @RequestParam int quantity,
                          @RequestParam(required = false) MultipartFile imageFile,
                          RedirectAttributes ra) {
    productRepository.findById(id).ifPresent(product -> {
        product.setPrice(price);
        product.setQuantity(quantity);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = fileUploadService.saveImage(imageFile);
                product.setImageUrl(imageUrl);
            } catch (Exception e) {
                // giữ ảnh cũ nếu lỗi
            }
        }
        productRepository.save(product);
    });
    ra.addFlashAttribute("success", "Product updated successfully!");
    return "redirect:/admin/products";
}
}