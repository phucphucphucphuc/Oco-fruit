package com.ocofruit.oco.Service;

import com.ocofruit.oco.Model.User;
import com.ocofruit.oco.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
<<<<<<< HEAD
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại!");
=======
            throw new RuntimeException("Username already existed!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already existed!");
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }
}