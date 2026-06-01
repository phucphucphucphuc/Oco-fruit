package com.ocofruit.oco.Controller;

import com.ocofruit.oco.Model.Order;
import com.ocofruit.oco.Model.User;
import com.ocofruit.oco.Model.UserProfile;
import com.ocofruit.oco.Repository.OrderRepository;
import com.ocofruit.oco.Repository.UserProfileRepository;
import com.ocofruit.oco.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired private UserRepository userRepository;
    @Autowired private UserProfileRepository userProfileRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
public String viewProfile(Authentication auth, Model model) {
    User user = userRepository.findByUsername(auth.getName()).orElseThrow();
    UserProfile profile = userProfileRepository.findByUserId(user.getId())
            .orElse(new UserProfile());

    List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

    model.addAttribute("page", "profile");
    model.addAttribute("user", user);
    model.addAttribute("profile", profile);
    model.addAttribute("orders", orders);
    return "profile";
}

    @PostMapping("/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String phone,
                                @RequestParam String address,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElse(new UserProfile());
        profile.setUser(user);
        profile.setFullName(fullName);
        profile.setPhone(phone);
        profile.setAddress(address);
        userProfileRepository.save(profile);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Wrong password!");
            return "redirect:/profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Password not match!");
            return "redirect:/profile";
        }
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "At least 6 characters!");
            return "redirect:/profile";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
        return "redirect:/profile";
    }
}