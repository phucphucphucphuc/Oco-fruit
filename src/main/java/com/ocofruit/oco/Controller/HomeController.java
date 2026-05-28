package com.ocofruit.oco.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Oco Fruit - Home");
        model.addAttribute("page", "home");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Oco Fruit - About");
        model.addAttribute("page", "about");
        return "about";
    }

    @GetMapping("/price")
    public String price(Model model) {
        model.addAttribute("title", "Oco Fruit - Price");
        model.addAttribute("page", "price");        
        return "price";
    }

    @GetMapping("/order")
    public String order(Model model) {
        model.addAttribute("title", "Oco Fruit - Order");
        model.addAttribute("page", "order");
        return "order";
    }
}