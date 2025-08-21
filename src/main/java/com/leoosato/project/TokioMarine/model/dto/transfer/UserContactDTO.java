package com.leoosato.project.TokioMarine.model.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContactDTO {
    private String fullName;
    private String accountNumber;
}