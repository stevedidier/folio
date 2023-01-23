package com.ftsd.folio.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    
    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader HttpHeaders header, Pageable pageable) {
        return new ResponseEntity<List<User>>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/admin/users/{userId}")
    public ResponseEntity<UserDto> getUserId(@RequestHeader HttpHeaders header, @PathVariable (value = "userId") Integer userId, Pageable pageable) {

        return new ResponseEntity<>(service.getUsersId(userId), HttpStatus.OK);
    }

    @PostMapping("/api/v1/admin/users")
    public ResponseEntity createUser(@RequestHeader HttpHeaders header, @Valid @RequestBody UserDto userDto) {
        service.addUser(userDto);
        return ResponseEntity.ok().build();
    }

    

    @PutMapping("/api/v1/dash/users/{userId}")
    public ResponseEntity updateUser(@RequestHeader HttpHeaders header, @PathVariable Integer userId, @Valid @RequestBody UserDto userRequest) {
        service.update(userRequest,userId );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/admin/users/role/{userId}")
    public ResponseEntity updateUserRole(@RequestHeader HttpHeaders header, @PathVariable Integer userId, @Valid @RequestBody String requestRole) {
        service.changeRole(requestRole,userId );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/dash/users/password/{userId}")
    public ResponseEntity updateUserPassword(@RequestHeader HttpHeaders header, @PathVariable Integer userId, @Valid @RequestBody RequestPassword requestPassword) {
        service.changePassword(requestPassword,userId );
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/api/v1/admin/users/{userId}")
    public ResponseEntity deleteUser(@RequestHeader HttpHeaders header, @PathVariable Integer userId) {

        service.delete(userId);
        return ResponseEntity.ok().build();
    }
}
