package com.pharmacy.web.config;


import com.pharmacy.web.entity.Role;
import com.pharmacy.web.entity.User;
import com.pharmacy.web.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("admin@pharmacy.com")) {

            User admin = User.builder()
                    .name("Admin")
                    .email("admin@pharmacy.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("9999999999")
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);

            System.out.println("Admin Created Successfully");
        }
    }
}