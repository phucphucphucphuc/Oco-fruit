package com.ocofruit.oco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ocofruit.oco.Repository.UserRepository;

@SpringBootApplication
public class OcoApplication {
    public static void main(String[] args) {
        
        
        SpringApplication.run(OcoApplication.class, args);
    }
@Autowired
private UserRepository userRepository;

@Autowired  
private PasswordEncoder passwordEncoder;

@Bean
CommandLineRunner initAdmin() {
    return args -> {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@ocofruit.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("Admin created!");
        } else {
            // Reset password
            userRepository.findByUsername("admin").ifPresent(u -> {
                u.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(u);
                System.out.println("Admin password reset!");
            });
        }
    };
}

}

