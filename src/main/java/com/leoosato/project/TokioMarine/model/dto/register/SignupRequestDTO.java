package com.leoosato.project.TokioMarine.model.dto.register;

import lombok.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {

    @NotBlank
    @Pattern(regexp="^[a-zA-Z0-9_\\.\\-]{3,40}$", message = "Username inválido")
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 60, message = "Senha deve ter 6 a 60 caracteres")
    private String password;
}