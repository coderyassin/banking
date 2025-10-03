package org.yascode.banking.services.impl;

import org.springframework.stereotype.Service;
import org.yascode.banking.dto.TransactionSumDetails;
import org.yascode.banking.models.TransactionType;
import org.yascode.banking.repositories.TransactionRepository;
import org.yascode.banking.services.StatisticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionRepository transactionRepository;

    public StatisticsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionSumDetails> findSumTractionsByDate(LocalDate startDate, LocalDate endDate, Integer userId) {
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        return transactionRepository.findSumTransactionsByDate(start, end, userId);
    }

    @Override
    public BigDecimal getAccountBalance(Integer userId) {
        return transactionRepository.findAccountBalance(userId);
    }

    @Override
    public BigDecimal highestTransfer(Integer userId) {
        return transactionRepository.findHighestAmountByTransactionType(userId, TransactionType.TRANSFERT);
    }

    @Override
    public BigDecimal highestDeposit(Integer userId) {
        return transactionRepository.findHighestAmountByTransactionType(userId, TransactionType.DEPOSIT);
    }
}
