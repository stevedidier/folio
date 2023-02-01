package com.ftsd.folio.user;

import com.ftsd.folio.exception.MessageResponse;
import com.ftsd.folio.exception.ResourceNotFoundException;
import com.ftsd.folio.util.Mappers;
import com.ftsd.folio.util.PwdPattern;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


import java.util.ArrayList;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;


@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public List<UserListDto> getAllUsers(){

    
    List<UserListDto> userList = new ArrayList<UserListDto>();

    for (User user : repository.findAll()) {

      userList.add(Mappers.toListDto(user));
      
    }

    return userList;
  }

  public UserDetailDto getUsersId(Integer userId){
    

    User user = repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));

    
    return Mappers.toDetailDto(user);
}
  

  public ResponseEntity<?> addUser(UserPostDto request) {

    ResponseEntity<?> responseEntity;

    if (PwdPattern.pwdValidate(request.getPassword()) && PwdPattern.emailValidate(request.getEmail())){
        
      if( !repository.existsByEmail(request.getEmail())){

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
        responseEntity = ResponseEntity.ok(new MessageResponse(" New user created successfully"));


        }else{
          responseEntity = ResponseEntity.badRequest().body(new MessageResponse("EmailError: Email already used "));

        }
      

    }else{
      responseEntity = ResponseEntity.badRequest().body(new MessageResponse("Error: Bad  password or email  "));
    }
    
    return responseEntity;
   
  } 

  public ResponseEntity<?> update(UserDetailDto userRequest, Integer userId){

    ResponseEntity<?> responseEntity = ResponseEntity.ok(new MessageResponse("User updated successfully"));

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    User  username = (User) auth.getPrincipal();
    int cond = username.getId() - userId;

    if((cond == 0 || username.getRole().name().equalsIgnoreCase(Role.ADMIN.name())) && PwdPattern.emailValidate(userRequest.getEmail())){

      Integer id = repository.findById(userId).map(user -> {
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setUpdatedAt(new Date());
        repository.save(user);

        return user.getId();

    }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));

    }else{
      responseEntity = ResponseEntity.badRequest().body(new MessageResponse("UserError: current user Id and userId doesn't match, or current user doesn't have right privilege, or email are invalid "));
     
    }

    return responseEntity;
  }

  public void delete(Integer userId){

    int i = repository.findById(userId).map(user -> {
        repository.delete(user);
        return 0;
    }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
 }

 public ResponseEntity<?> changePassword(RequestPasswordDto request, Integer userId){

  ResponseEntity<?> responseEntity;


      Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      User  username = (User) auth.getPrincipal();
  
      int cond = username.getId() - userId;
  
      if(cond == 0 && PwdPattern.pwdValidate(request.getNewPassword()) && !(request.getNewPassword().equalsIgnoreCase(request.getOldPassword())) ){
  
              boolean result = repository.findById(userId).map(user -> {

                boolean bool = false;
              
                if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())){

                  user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                  user.setUpdatedAt(new Date());
                  repository.save(user);

                  bool = true;
                }
          
     
            return bool;

          }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));

          if(result){
            responseEntity = ResponseEntity.ok(new MessageResponse(" Password updated successfully !"));
         
          }else{
            responseEntity = ResponseEntity.badRequest().body(new MessageResponse("PasswordError: old password  doesn't match"));
   
          }
     
      }else{
        responseEntity = ResponseEntity.badRequest().body(new MessageResponse("IdError: current user Id and userId doesn't match, or new password invalid or same as previous password "));
        

      }
  
    
    return responseEntity;
    
  }

  public ResponseEntity<?>  changeRole(RoleRequestDto requestRole, Integer userId){

    ResponseEntity<?> responseEntity = ResponseEntity.badRequest().body(new MessageResponse("RoleError: Role doesn't exist"));
    
   

    boolean result = repository.findById(userId).map(user -> {
            
            boolean bool = false;
            if(requestRole.getRole().equalsIgnoreCase(Role.ADMIN.name()) ){
                user.setRole(Role.ADMIN);
                user.setUpdatedAt(new Date());
                repository.save(user);
                bool = true;
                
            }else if(requestRole.getRole().equalsIgnoreCase(Role.USER.name()) ){
                user.setRole(Role.USER);
                user.setUpdatedAt(new Date());
                repository.save(user);
                bool = true;

            }

            return bool;

        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));

        if(result){
          responseEntity = ResponseEntity.ok(new MessageResponse(" Role updated successfully !"));
          
        }

        return responseEntity;


  }

    
}
