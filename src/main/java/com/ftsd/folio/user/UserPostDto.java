package com.ftsd.folio.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserPostDto {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
    
}
