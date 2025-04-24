package com.ordem.servico.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String username;
    private String password;
    private String email;
    private List<String> permissions;

}