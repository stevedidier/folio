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

public class UserPost {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
    
}
