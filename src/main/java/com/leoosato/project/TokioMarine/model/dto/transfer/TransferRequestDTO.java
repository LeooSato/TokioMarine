package com.leoosato.project.TokioMarine.model.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

    @Pattern(regexp="\\d{10}", message="Conta origem deve ter 10 dígitos")
    @NotBlank
    private String accountFrom;

    @Pattern(regexp="\\d{10}", message="Conta destino deve ter 10 dígitos")
    @NotBlank
    private String accountTo;

    @NotNull
    @DecimalMin(value="0.01", message="Valor deve ser maior que 0")
    private BigDecimal amount;

    @NotNull(message="Data de transferência é obrigatória")
    private LocalDate transferDate;
}
