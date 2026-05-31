package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Model.CartItem;
import com.ocofruit.oco.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // Trang cart
    @GetMapping("/cart")
public String cartPage(Authentication auth, Model model) {
    String username = auth.getName();
    List<List<CartItem>> boxes = cartService.getCartBoxes(username);
    double total = cartService.getTotalPrice(username);
    long completeBoxCount = cartService.countCompleteBoxes(username);
    boolean hasIncompleteBox = cartService.hasIncompleteBox(username);

    model.addAttribute("boxes", boxes);
    model.addAttribute("total", total);
    model.addAttribute("completeBoxCount", completeBoxCount);
    model.addAttribute("hasIncompleteBox", hasIncompleteBox);
    model.addAttribute("page", "cart");
    return "cart";
}

    // Thêm vào cart từ trang order
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam List<Long> fruitIds,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(auth.getName(), fruitIds);
            redirectAttributes.addFlashAttribute("success", "Đã thêm vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/order";
    }

    // Xóa item
    @PostMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return "redirect:/cart";
    }

    // Xóa toàn bộ cart
    @PostMapping("/cart/clear")
    public String clearCart(Authentication auth) {
        cartService.clearCart(auth.getName());
        return "redirect:/cart";
    }
}