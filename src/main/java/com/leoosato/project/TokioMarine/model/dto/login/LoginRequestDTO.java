package com.leoosato.project.TokioMarine.model.dto.login;

import lombok.*;
import javax.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank private String username;
    @NotBlank private String password;
}