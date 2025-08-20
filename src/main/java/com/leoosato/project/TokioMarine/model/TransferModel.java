package com.leoosato.project.TokioMarine.model;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // números mantidos para exibição/auditoria
    @Column(nullable = false, length = 10)
    private String accountFrom;

    @Column(nullable = false, length = 10)
    private String accountTo;

    // relações para integridade
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "from_user_id", nullable = false)
    private UserModel fromUser;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "to_user_id", nullable = false)
    private UserModel toUser;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false)
    private LocalDate scheduleDate;

    @Column(nullable = false)
    private LocalDate transferDate;
}
