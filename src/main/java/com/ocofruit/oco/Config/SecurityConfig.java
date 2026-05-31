package com.ocofruit.oco.Config;

import com.ocofruit.oco.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
<<<<<<< HEAD
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public + static assets
                .requestMatchers("/", "/about", "/price", "/register", "/login",
                 "/assets/**", "/css/**", "/js/**", "/fonts/**",
                 "/favicon_io/**", "/images/**", "/webjars/**",
                 "/gen-hash").permitAll()  // ← thêm dòng này
                // User + Staff + Admin
                .requestMatchers("/order", "/cart/**", "/checkout/**", "/profile/**").hasAnyRole("USER", "STAFF", "ADMIN")
                // Staff + Admin
                .requestMatchers("/staff/**").hasAnyRole("STAFF", "ADMIN")
                // Admin only
                .requestMatchers("/admin/**").hasRole("ADMIN")
=======
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public - ai cũng vào được
                .requestMatchers("/", "/about", "/price", "/register", "/login").permitAll()
                .requestMatchers("/css/**", "/js/**", "/assets/**", "/favicon_io/**").permitAll()
                // Chỉ ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Phải đăng nhập
                .requestMatchers("/order", "/order/**").hasAnyRole("USER", "ADMIN")
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
<<<<<<< HEAD
=======
                .failureUrl("/login?error=true")
>>>>>>> 130961b1d5aec426173659935509f03071d3702f
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
<<<<<<< HEAD
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/cart/**", "/checkout/**"));

        return http.build();
    }
=======
                .logoutSuccessUrl("/")  // về trang Home
                .permitAll()
            );

        return http.build();
    }

>>>>>>> 130961b1d5aec426173659935509f03071d3702f
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
               .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}