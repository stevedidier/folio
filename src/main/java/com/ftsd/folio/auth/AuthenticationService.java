package com.ftsd.folio.auth;

import com.ftsd.folio.config.JwtService;
import com.ftsd.folio.user.Role;
import com.ftsd.folio.user.User;
import com.ftsd.folio.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.security.authentication.BadCredentialsException;
import com.ftsd.folio.exception.ResourceNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import com.ftsd.folio.exception.MessageResponse;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER) 
        .build();
    user.setCreatedAt(new Date());
    user.setUpdatedAt(new Date());
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    AuthenticationResponse authenticationResponse = new AuthenticationResponse() ;
    
    try {

        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                    )
                );

        var user = repository.findByEmail(request.getEmail()).map(usr -> {

            
            usr.setResetPasswordToken(null);
            usr.setUpdatedAt(new Date());
            repository.save(usr);
        
            return usr;
        
        }).orElseThrow(() -> new ResourceNotFoundException("Email " + request.getEmail() + " not found"));
            
            var jwtToken = jwtService.generateToken(user);
            authenticationResponse.setToken(jwtToken);
            authenticationResponse.setIdUser(user.getId());
            authenticationResponse.setEmail(user.getEmail());
            authenticationResponse.setRole(user.getRole().name());
            authenticationResponse.setUsername(user.getUsername());
            authenticationResponse.setType("Bearer");

        
    } catch (BadCredentialsException e) {
        authenticationResponse.setBadCredentials(true);
    }

    return authenticationResponse;

            
        }

    
        public ResponseMessageDto updateResetPasswordToken(PasswordResetRequestDto request){

            ResponseMessageDto response = new ResponseMessageDto();
            response.setStatus(404);
        
            String result = repository.findByEmail(request.getEmail()).map(user -> {
        
              String token = RandomStringUtils.random(30);
        
              user.setResetPasswordToken(token);
        
              user.setUpdatedAt(new Date());
              repository.save(user);
        
              return token;
        
          }).orElseThrow(() -> new ResourceNotFoundException("Email " + request.getEmail() + " not found"));
        
          response.setEmail(request.getEmail());
          response.setToken(result);
          response.setStatus(200);
          
          return response;
        
    }

    public void updatePassword(User user, String newPassword) {
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        repository.save(user);
        
    }

    public ResponseEntity<?> getByResetPasswordToken(ResetPasswordRequestDto request) {

        ResponseEntity<?> responseEntity = ResponseEntity.badRequest().body(new MessageResponse("You've encountered some errors while trying to reset your password."));

        Integer id = repository.findByResetPasswordToken(request.getToken()).map(user -> {
        
          updatePassword(user, request.getPassword());
       
            return user.getId();
      
        }).orElseThrow(() -> new ResourceNotFoundException(" Invalid token"));

        responseEntity = ResponseEntity.ok(new MessageResponse("You've successfully reset your password."));
      
        return responseEntity;



    }



        


}
