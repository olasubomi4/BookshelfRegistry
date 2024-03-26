package com.group5.bookshelfregistry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserRequest {

    @NotBlank(message =  "username cannot be blank")
    @NonNull
    private String username;

    @NotBlank(message =  "password cannot be blank")
    @NonNull
    private String password;
}
