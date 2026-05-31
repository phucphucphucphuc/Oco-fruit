package com.ocofruit.oco.Service;

import com.ocofruit.oco.Model.User;
import com.ocofruit.oco.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));
<<<<<<< HEAD
        
            // DEBUG - xóa sau khi fix
    System.out.println("=== LOGIN DEBUG ===");
    System.out.println("Username: " + user.getUsername());
    System.out.println("Password hash: " + user.getPassword());
    System.out.println("Role: " + user.getRole());
    System.out.println("Enabled: " + user.getEnabled());
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getEnabled(),  // enabled
            true,               // accountNonExpired
            true,               // credentialsNonExpired
            true,               // accountNonLocked
=======

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}