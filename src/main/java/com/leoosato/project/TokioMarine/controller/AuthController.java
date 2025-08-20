package com.leoosato.project.TokioMarine.controller;


import com.leoosato.project.TokioMarine.model.dto.login.AuthResponseDTO;
import com.leoosato.project.TokioMarine.model.dto.login.LoginRequestDTO;
import com.leoosato.project.TokioMarine.model.dto.register.SignupRequestDTO;
import com.leoosato.project.TokioMarine.model.dto.register.UserResponseDTO;
import com.leoosato.project.TokioMarine.security.JwtUtil;
import com.leoosato.project.TokioMarine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@Valid @RequestBody SignupRequestDTO dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer"));
    }
}