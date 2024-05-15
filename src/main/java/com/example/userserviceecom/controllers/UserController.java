package com.example.userserviceecom.controllers;


import com.example.userserviceecom.dtos.LoginRequestDto;
import com.example.userserviceecom.dtos.LogoutRequestDto;
import com.example.userserviceecom.dtos.SignUpRequestDto;
import com.example.userserviceecom.dtos.UserDto;
import com.example.userserviceecom.models.Token;
import com.example.userserviceecom.models.User;
import com.example.userserviceecom.services.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp( @RequestBody SignUpRequestDto signUpRequestDto ){
        User user = userService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
        );
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login( @RequestBody LoginRequestDto requestDto){

        return null;
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto){
        return null;
    }
    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        return null;
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return null;
    }
}
