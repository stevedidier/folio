package com.ftsd.folio.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ResponseMessageDto {
    private Integer status;
    private String token;
    private String email;
    
}
