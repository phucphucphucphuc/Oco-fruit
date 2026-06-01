package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Service.RecaptchaService;
import com.ocofruit.oco.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecaptchaService recaptchaService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) model.addAttribute("error", "Wrong username or password!");
        if (logout != null) model.addAttribute("success", "Logged out successfully!");
        model.addAttribute("page", "login");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("page", "register");
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(name = "g-recaptcha-response", required = false) String captcha,
            RedirectAttributes redirectAttributes) {

        // Verify captcha
       // if (!recaptchaService.verify(captcha)) {
           // redirectAttributes.addFlashAttribute("error", "Please complete the CAPTCHA!");
         //   return "redirect:/register";
       // }

        try {
            userService.register(username, email, password);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}