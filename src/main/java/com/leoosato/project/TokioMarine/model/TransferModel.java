package com.leoosato.project.TokioMarine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String accountFrom;

    @Column(nullable = false, length = 10)
    private String accountTo;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal fee; // taxa calculada

    @Column(nullable = false)
    private LocalDate scheduleDate;   // hoje

    @Column(nullable = false)
    private LocalDate transferDate;

}
