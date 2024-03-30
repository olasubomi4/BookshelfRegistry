package com.group5.bookshelfregistry.dto.user;

import com.group5.bookshelfregistry.dto.bookshelf.response.BookShelfResponseData;
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
