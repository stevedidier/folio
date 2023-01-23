package com.ftsd.folio.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPassword {

    private String oldPassword;
    private String newPassword;

    
}
