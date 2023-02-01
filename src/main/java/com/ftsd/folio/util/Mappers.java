package com.ftsd.folio.util;

import com.ftsd.folio.image.Image;
import com.ftsd.folio.image.ImageDto;
import com.ftsd.folio.user.User;
import com.ftsd.folio.user.UserDetailDto;
import com.ftsd.folio.user.UserListDto;

public class Mappers {

    public static UserDetailDto toDetailDto(User user) {

        UserDetailDto userDto = new UserDetailDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setPassword(user.getPassword());

        return userDto;
     }


     public static UserListDto toListDto(User user) {

        UserListDto userDto = new UserListDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setRole(user.getRole().name());
        userDto.setUsername(user.getUsername());

        return userDto;
     }


     public static ImageDto toImageDto(Image image) {

      ImageDto imageDto = new ImageDto();
      imageDto.setId(image.getId());
      imageDto.setTitle(image.getTitle());
      imageDto.setDescription(image.getDescription());
      imageDto.setUrl(image.getUrl());
      imageDto.setType(image.getType().name());
      imageDto.setSize(image.getSize());
      imageDto.setFilename(image.getFilename());
      
    
      return imageDto;
   }
    
}
