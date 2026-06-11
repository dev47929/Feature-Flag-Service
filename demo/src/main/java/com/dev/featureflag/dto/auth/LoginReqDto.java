package com.dev.featureflag.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginReqDto {
    private String username;
    private String password;
}
