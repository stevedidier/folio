package com.ftsd.folio.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private String token;
  private Integer idUser;  
  private String role;
  private String username;
  private String email;
  private String type ;
  private boolean badCredentials;
}