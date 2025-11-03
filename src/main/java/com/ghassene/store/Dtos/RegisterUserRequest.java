package com.ghassene.store.Dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
