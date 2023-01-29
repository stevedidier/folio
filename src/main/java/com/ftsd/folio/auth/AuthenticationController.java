package com.ftsd.folio.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import jakarta.validation.Valid;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/forgot_password")
  public ResponseEntity<?> processForgotPassword(@RequestHeader HttpHeaders header, @Valid @RequestBody PasswordResetRequestDto passwordResetRequest) {
      return service.updateResetPasswordToken( passwordResetRequest );
  }

  @PostMapping("/reset_password")
  public ResponseEntity<?> processResetPassword(@RequestHeader HttpHeaders header, @Valid @RequestBody ResetPasswordRequestDto request) {
      return service.getByResetPasswordToken( request );
  }

}
