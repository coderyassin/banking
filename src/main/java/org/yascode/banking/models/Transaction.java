package org.yascode.banking.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractEntity {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "destination_iban")
    private String destinationIban;

    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
