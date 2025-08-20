package com.leoosato.project.TokioMarine.model.dto.login;
import lombok.*;

@Data @AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tokenType; // "Bearer"
}