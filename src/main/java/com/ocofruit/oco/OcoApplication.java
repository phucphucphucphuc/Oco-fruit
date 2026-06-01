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

    ;
}



