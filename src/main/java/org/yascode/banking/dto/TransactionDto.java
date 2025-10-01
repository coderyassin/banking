package org.yascode.banking.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.yascode.banking.models.Transaction;
import org.yascode.banking.models.TransactionType;
import org.yascode.banking.models.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionDto {

  private Integer id;

  @Positive
  private BigDecimal amount;

  private TransactionType type;

  private String destinationIban;

  private LocalDate transactionDate;

  private Integer userId;

  public static TransactionDto fromEntity(Transaction transaction) {
    return TransactionDto.builder()
        .id(transaction.getId())
        .amount(transaction.getAmount())
        .type(transaction.getType())
        .transactionDate(transaction.getTransactionDate())
        .destinationIban(transaction.getDestinationIban())
        .userId(transaction.getUser().getId())
        .build();
  }

  public static Transaction toEntity(TransactionDto transaction) {
    return Transaction.builder()
        .id(transaction.getId())
        .amount(transaction.getAmount())
        .type(transaction.getType())
        .transactionDate(LocalDate.now())
        .destinationIban(transaction.getDestinationIban())
        .user(
            User.builder()
                .id(transaction.getUserId())
                .build()
            )
        .build();
  }

}
