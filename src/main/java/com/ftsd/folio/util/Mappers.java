package com.ftsd.folio.util;

import com.ftsd.folio.user.User;
import com.ftsd.folio.user.UserDetailDto;
import com.ftsd.folio.user.UserListDto;

public class Mappers {

    public static UserDetailDto toDetailDto(User user) {

        UserDetailDto userDto = new UserDetailDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setPassword(user.getPassword());

        return userDto;
     }


     public static UserListDto toListDto(User user) {

        UserListDto userDto = new UserListDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setRole(user.getRole().name());
        userDto.setUsername(user.getUsername());

        return userDto;
     }
    
}
