package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Model.CartItem;
import com.ocofruit.oco.Model.Order;
import com.ocofruit.oco.Service.CartService;
import com.ocofruit.oco.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkoutPage(Authentication auth, Model model) {
        String username = auth.getName();
        List<List<CartItem>> boxes = cartService.getCartBoxes(username);
        double total = cartService.getTotalPrice(username);

        if (boxes.isEmpty()) return "redirect:/cart";

        model.addAttribute("boxes", boxes);
        model.addAttribute("total", total);
        model.addAttribute("page", "checkout");
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(
            @RequestParam String customerName,
            @RequestParam String email,
            @RequestParam String customerPhone,
            @RequestParam String address,
            @RequestParam(required = false) String note,
            @RequestParam String paymentMethod,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            String username = auth.getName();
            List<CartItem> items = cartService.getCartItems(username);
            List<Long> fruitIds = items.stream()
                .map(item -> item.getProduct().getId())
                .collect(Collectors.toList());

            Order order = orderService.createOrder(customerName, customerPhone, address, fruitIds);
            cartService.clearCart(username);

            redirectAttributes.addFlashAttribute("success",
                "Order placed successfully! Order ID: #" + order.getId());
            return "redirect:/order-success/" + order.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
}

