package com.ghassene.store.Dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
}
