package com.example.userserviceecom.dtos;

import com.example.userserviceecom.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Token token;
}
