package com.ftsd.folio.util;

import com.ftsd.folio.user.User;
import com.ftsd.folio.user.UserDto;

public class Mappers {

    public static UserDto toDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setPassword(user.getPassword());

        return userDto;
     }
    
}
