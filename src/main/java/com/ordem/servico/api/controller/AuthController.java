package com.ordem.servico.api.controller;

import com.ordem.servico.api.dto.AuthRequest;
import com.ordem.servico.api.dto.RegisterRequest;
import com.ordem.servico.api.model.Permission;
import com.ordem.servico.api.model.User;
import com.ordem.servico.api.repository.PermissionRepository;
import com.ordem.servico.api.repository.UserRepository;
import com.ordem.servico.api.response.AuthResponse;
import com.ordem.servico.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PermissionRepository permissionRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username já está em uso.");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setAdmin(false);
        Set<Permission> permissions = new HashSet<>();
        for (String permissionName : registerRequest.getPermissions()) {
            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseThrow(() -> new RuntimeException("Permissão não encontrada: " + permissionName));
            permissions.add(permission);
        }
        user.setPermissions(permissions);
        userRepository.save(user);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
}