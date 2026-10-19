package com.dev.featureflag.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupReqDto {
    private String username;
    private String password;
    private String name;
    private String email;
}
