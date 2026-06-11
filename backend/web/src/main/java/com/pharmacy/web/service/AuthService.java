package com.pharmacy.web.service;



import com.pharmacy.web.dto.*;
import com.pharmacy.web.entity.*;
import com.pharmacy.web.Repository.UserRepository;
import com.pharmacy.web.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(
                        passwordEncoder.encode(request.password())
                )
                .role(Role.ROLE_CUSTOMER)
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token =
                jwtService.generateToken(request.email());

        return new AuthResponse(token);
    }
}
