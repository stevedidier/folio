package com.ftsd.folio.auth;

import com.ftsd.folio.config.JwtService;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  private JavaMailSender mailSender;

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

    
    public ResponseEntity<?> updateResetPasswordToken(PasswordResetRequestDto request){

        ResponseEntity<?> responseEntity = ResponseEntity.badRequest().body(new MessageResponse("Email " + request.getEmail() + " not found"));
    
        String result = repository.findByEmail(request.getEmail()).map(user -> {
    
            String token = RandomStringUtils.randomAlphanumeric(30);
    
            user.setResetPasswordToken(token);
    
            user.setUpdatedAt(new Date());
            repository.save(user);
    
            return token;
    
        }).orElseThrow(() -> new ResourceNotFoundException("Email " + request.getEmail() + " not found"));
    
        
        MimeMessage msg;
        MimeMessageHelper helper;
        try {

            msg = mailSender.createMimeMessage();
            String contextPath = "http://localhost:3000";
            String link = contextPath + "/resetpassword?token=" + result;
            String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
            
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(request.getEmail());
            helper.setSubject("Here's the link to reset your password");
            helper.setText(content, true);
            mailSender.send(msg);
            //sendEmailWithAttachment();

        } catch (MessagingException e) {
            e.printStackTrace();
            responseEntity = ResponseEntity.ok(new MessageResponse("Error during process seding mail."));
        } 

        responseEntity = ResponseEntity.ok(new MessageResponse("Email has send successfully."));
        
       return  responseEntity;
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


    public User findByResetPasswordToken(String token){

        User user = null;
        user = repository.findByResetPasswordToken(token).map(usr -> {
         
              return usr;
        
          }).orElseThrow(() -> new ResourceNotFoundException(" Invalid token"));

        return user;
    }


        


}
