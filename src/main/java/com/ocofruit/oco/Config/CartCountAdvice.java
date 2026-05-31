<<<<<<< HEAD
package com.ocofruit.oco.Config;
=======
wpackage com.ocofruit.oco.Config;
>>>>>>> 130961b1d5aec426173659935509f03071d3702f

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