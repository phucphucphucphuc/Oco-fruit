package com.ocofruit.oco.Config;

import com.ocofruit.oco.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CartCountAdvice {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartCount")
    public long cartCount(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return 0;
        return cartService.countBoxes(auth.getName());
    }
}