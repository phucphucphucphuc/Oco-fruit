package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Oco Fruit - Home");
        model.addAttribute("page", "home");
        model.addAttribute("products", productService.getAllProducts());
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
        model.addAttribute("products", productService.getAllProducts());        
        return "price";
    }
}