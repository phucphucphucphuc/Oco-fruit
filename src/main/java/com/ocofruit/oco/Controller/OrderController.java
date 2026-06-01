package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Model.Order;
import com.ocofruit.oco.Service.OrderService;
import com.ocofruit.oco.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    // order (GET)
    @GetMapping("/order")
    public String orderPage(Model model) {
        model.addAttribute("title", "Oco Fruit - Order");
        model.addAttribute("page", "order");

        model.addAttribute("fruits", productService.getProductsByCategory("fruit"));
        model.addAttribute("juices", productService.getProductsByCategory("juice"));
        model.addAttribute("mixes", productService.getProductsByCategory("mix"));

        return "order";
    }

    // Submit (POST)
    @PostMapping("/order")
    public String submitOrder(
            @RequestParam String customerName,
            @RequestParam String customerPhone,
            @RequestParam String address,
            @RequestParam List<Long> fruitIds,
            RedirectAttributes redirectAttributes) {

        try {
            Order order = orderService.createOrder(customerName, customerPhone, address, fruitIds);
            redirectAttributes.addFlashAttribute("success",
                "Đặt hàng thành công! Mã đơn: #" + order.getId());
            return "redirect:/order/success/" + order.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/order";
        }
    }

    // order acp
    @GetMapping("/order-success/{id}")
    public String orderSuccess(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("title", "Order Successful");
        return "order-success";
    }
}