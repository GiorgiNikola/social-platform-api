package com.socialplatformapi.dto.register;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Size of username should be between 3 and 30")
    private String username;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotBlank(message = "email is required")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 20, message = "Size of password should be between 6 and 20")
    private String password;
}
