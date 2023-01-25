package com.ftsd.folio.user;

import com.ftsd.folio.exception.ResourceNotFoundException;
import com.ftsd.folio.util.Mappers;
import com.ftsd.folio.util.PwdPattern;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  
  
  

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
  

  public void addUser(UserPostDto request) {

    if (PwdPattern.pwdValidate(request.getPassword())){

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

    }else{
      System.out.print("The Password " + request.getPassword() + " isn't valid");
    }
    
   
  } 

  public void update(UserDetailDto userRequest, Integer userId){

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    User  username = (User) auth.getPrincipal();
    int cond = username.getId() - userId;

    if(cond == 0){

      Integer id = repository.findById(userId).map(user -> {
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setUpdatedAt(new Date());
        repository.save(user);

        return user.getId();

    }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));

    }else{
      System.out.println(username.getId() + " not match "+ userId);
    }

    
  }

  public void delete(Integer userId){

    int i = repository.findById(userId).map(user -> {
        repository.delete(user);
        return 0;
    }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
 }

 public void changePassword(RequestPasswordDto request, Integer userId){

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    User  username = (User) auth.getPrincipal();

    int cond = username.getId() - userId;

    if(cond == 0){

              Integer id = repository.findById(userId).map(user -> {
              
                authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                      request.getOldPassword()
                  )
                );
          
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                user.setUpdatedAt(new Date());
                repository.save(user);
          
            return user.getId();

        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));

    }else{
      System.out.println(username.getId() + "  Not match  "+ userId);
    }


    
  }

  public void changeRole(RoleRequestDto requestRole, Integer userId){
    
    Integer id = repository.findById(userId).map(user -> {
            if(requestRole.getRole().equalsIgnoreCase(Role.ADMIN.name()) ){
                user.setRole(Role.ADMIN);
                user.setUpdatedAt(new Date());
                repository.save(user);
            }else if(requestRole.getRole().equalsIgnoreCase(Role.USER.name()) ){
                user.setRole(Role.USER);
                user.setUpdatedAt(new Date());
                repository.save(user);

            }

            return user.getId();

        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
  }

    
}
