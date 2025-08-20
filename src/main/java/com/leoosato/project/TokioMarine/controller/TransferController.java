package com.leoosato.project.TokioMarine.controller;

import com.leoosato.project.TokioMarine.model.dto.transfer.TransferRequestDTO;
import com.leoosato.project.TokioMarine.model.dto.transfer.TransferResponseDTO;
import com.leoosato.project.TokioMarine.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDTO create(@Valid @RequestBody TransferRequestDTO dto) {
        return service.schedule(dto);
    }

    @GetMapping
    public List<TransferResponseDTO> list() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public TransferResponseDTO getById(@PathVariable Long id) {
        return service.listAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada"));
    }
}