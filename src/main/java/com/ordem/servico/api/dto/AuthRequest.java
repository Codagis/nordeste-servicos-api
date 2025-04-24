package com.ordem.servico.api.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}