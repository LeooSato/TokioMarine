package com.leoosato.project.TokioMarine.model.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDTO {
    private Long id;
    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;
    private BigDecimal fee;
    private LocalDate scheduleDate;
    private LocalDate transferDate;
}