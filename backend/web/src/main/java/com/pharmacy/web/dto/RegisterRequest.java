package com.pharmacy.web.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank
        String name,

        @Email
        String email,

        @Size(min = 6)
        String password,

        @Pattern(regexp = "^[0-9]{10}$")
        String phone

) {
}
