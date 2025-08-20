package com.leoosato.project.TokioMarine.model.dto.register;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Instant createdAt;
    private String accountNumber;
}