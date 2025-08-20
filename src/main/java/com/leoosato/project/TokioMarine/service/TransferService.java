package com.leoosato.project.TokioMarine.service;

import com.leoosato.project.TokioMarine.exception.BusinessException;
import com.leoosato.project.TokioMarine.model.TransferModel;
import com.leoosato.project.TokioMarine.model.dto.transfer.TransferRequestDTO;
import com.leoosato.project.TokioMarine.model.dto.transfer.TransferResponseDTO;
import com.leoosato.project.TokioMarine.repository.TransferRepository;
import com.leoosato.project.TokioMarine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository repo;
    private final UserRepository userRepo;

    public TransferResponseDTO schedule(TransferRequestDTO dto) {
        LocalDate today = LocalDate.now();
        if (dto.getTransferDate().isBefore(today)) throw new BusinessException("Data de transferência não pode ser no passado.");

        // usuário autenticado
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        var currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new BusinessException("Usuário autenticado não encontrado."));

        // valida contas
        var fromUser = userRepo.findByAccountNumber(dto.getAccountFrom())
                .orElseThrow(() -> new BusinessException("Conta de origem inexistente."));
        var toUser = userRepo.findByAccountNumber(dto.getAccountTo())
                .orElseThrow(() -> new BusinessException("Conta de destino inexistente."));

        if (!fromUser.getId().equals(currentUser.getId())) {
            throw new BusinessException("Você só pode transferir a partir da sua própria conta.");
        }
        if (fromUser.getId().equals(toUser.getId())) {
            throw new BusinessException("Conta de origem e destino não podem ser a mesma.");
        }

        long days = ChronoUnit.DAYS.between(today, dto.getTransferDate());
        BigDecimal fee = calcFee(dto.getAmount(), (int) days).setScale(2, RoundingMode.HALF_UP);

        TransferModel t = TransferModel.builder()
                .accountFrom(dto.getAccountFrom())
                .accountTo(dto.getAccountTo())
                .fromUser(fromUser)
                .toUser(toUser)
                .amount(dto.getAmount().setScale(2, RoundingMode.HALF_UP))
                .fee(fee)
                .scheduleDate(today)
                .transferDate(dto.getTransferDate())
                .build();

        t = repo.save(t);
        return toDTO(t);
    }

    public List<TransferResponseDTO> listAll() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    private TransferResponseDTO toDTO(TransferModel t) {
        return TransferResponseDTO.builder()
                .id(t.getId())
                .accountFrom(t.getAccountFrom())
                .accountTo(t.getAccountTo())
                .amount(t.getAmount())
                .fee(t.getFee())
                .scheduleDate(t.getScheduleDate())
                .transferDate(t.getTransferDate())
                .build();
    }

    private BigDecimal calcFee(java.math.BigDecimal amount, int days) {
        if (days < 0) throw new BusinessException("Dias inválidos.");

        java.math.BigDecimal fixed = java.math.BigDecimal.ZERO;
        java.math.BigDecimal perc  = java.math.BigDecimal.ZERO;

        if (days == 0) { fixed = new java.math.BigDecimal("3.00");  perc = new java.math.BigDecimal("0.025"); }
        else if (days <= 10) { fixed = new java.math.BigDecimal("12.00"); }
        else if (days <= 20) { perc  = new java.math.BigDecimal("0.082"); }
        else if (days <= 30) { perc  = new java.math.BigDecimal("0.069"); }
        else if (days <= 40) { perc  = new java.math.BigDecimal("0.047"); }
        else if (days <= 50) { perc  = new java.math.BigDecimal("0.017"); }
        else { throw new BusinessException("Não há taxa aplicável para mais de 50 dias."); }

        return fixed.add(amount.multiply(perc)); // arredondamento é feito na schedule()
    }

}

