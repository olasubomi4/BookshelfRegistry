package com.group5.bookshelfregistry.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserResponse
{
    private String token;
    private Integer tokenExpiration;
}
