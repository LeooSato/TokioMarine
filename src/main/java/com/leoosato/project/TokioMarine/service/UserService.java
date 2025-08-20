package com.leoosato.project.TokioMarine.service;

import com.leoosato.project.TokioMarine.exception.BusinessException;
import com.leoosato.project.TokioMarine.model.UserModel;
import com.leoosato.project.TokioMarine.model.dto.register.SignupRequestDTO;
import com.leoosato.project.TokioMarine.model.dto.register.UserResponseDTO;
import com.leoosato.project.TokioMarine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponseDTO register(SignupRequestDTO dto) {
        boolean userTaken  = userRepository.existsByUsername(dto.getUsername());
        boolean emailTaken = userRepository.existsByEmail(dto.getEmail());

        BusinessException ex =
                userTaken  ? new BusinessException("Username já em uso.") :
                        emailTaken ? new BusinessException("Email já em uso.") :
                                null;

        if (ex != null) throw ex;

        UserModel user = UserModel.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .build();

        user = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
