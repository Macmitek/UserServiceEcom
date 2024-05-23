package com.example.userserviceecom.controllers;


import com.example.userserviceecom.dtos.*;
import com.example.userserviceecom.models.Token;
import com.example.userserviceecom.models.User;
import com.example.userserviceecom.services.UserService;
import exceptions.InvalidPasswordException;
import exceptions.InvalidTokenException;
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
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) throws InvalidPasswordException {
        Token token =  userService.login(requestDto.getEmail(), requestDto.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token);
        return loginResponseDto;
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto) throws InvalidTokenException {
        ResponseEntity<Void> responseEntity = null;
        try {
           userService.logout(requestDto.getToken());
           responseEntity = ResponseEntity.ok().build();
       }catch (Exception e){
           System.out.println("Something went wrong");
           responseEntity = ResponseEntity.badRequest().build();
       }
        return responseEntity;
    }
    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) throws InvalidTokenException {
        return UserDto.from(userService.validateToken(token));
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return null;
    }
}
