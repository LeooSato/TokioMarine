package com.leoosato.project.TokioMarine.service;

import com.leoosato.project.TokioMarine.exception.BusinessException;
import com.leoosato.project.TokioMarine.model.UserModel;
import com.leoosato.project.TokioMarine.model.dto.register.SignupRequestDTO;
import com.leoosato.project.TokioMarine.model.dto.register.UserResponseDTO;
import com.leoosato.project.TokioMarine.model.dto.transfer.UserContactDTO;
import com.leoosato.project.TokioMarine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom random = new SecureRandom();

    public UserResponseDTO register(SignupRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) throw new BusinessException("Username já em uso.");
        if (userRepository.existsByEmail(dto.getEmail())) throw new BusinessException("Email já em uso.");

        String accountNumber = generateUniqueAccountNumber();

        UserModel user = UserModel.builder()
                .fullName(dto.getFullName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .accountNumber(accountNumber)
                .role("USER")
                .build();

        user = userRepository.save(user);

        return UserResponseDTO.builder()
                .fullName(dto.getFullName())
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private String generateUniqueAccountNumber() {
        String num;
        do {
            num = String.format("%010d", random.nextInt(1_000_000_000));
        } while (userRepository.existsByAccountNumber(num));
        return num;
    }

    public List<UserContactDTO> listContacts() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : null;

        return userRepository.findAll().stream()
                .filter(u -> currentUsername == null || !u.getUsername().equals(currentUsername))
                .map(u -> UserContactDTO.builder()
                        .fullName(u.getFullName())
                        .accountNumber(u.getAccountNumber())
                        .build())
                .collect(Collectors.toList());
    }


}
