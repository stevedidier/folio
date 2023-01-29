package com.ftsd.folio.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResetPasswordRequestDto {

    private String token;
    private String password;
    
}
