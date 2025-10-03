package org.yascode.banking.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.yascode.banking.dto.TransactionDto;
import org.yascode.banking.models.Transaction;
import org.yascode.banking.models.TransactionType;
import org.yascode.banking.repositories.TransactionRepository;
import org.yascode.banking.services.TransactionService;
import org.yascode.banking.validators.ObjectsValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ObjectsValidator<TransactionDto> validator;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ObjectsValidator<TransactionDto> validator) {
        this.transactionRepository = transactionRepository;
        this.validator = validator;
    }

    @Override
    public List<TransactionDto> findAllByUserId(Integer userId) {
        return transactionRepository.findAllByUserId(userId)
                .stream()
                .map(TransactionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Integer save(TransactionDto dto) {
        validator.validate(dto);
        Transaction transaction = TransactionDto.toEntity(dto);
        BigDecimal transactionMultiplier = BigDecimal.valueOf(getTransactionMultiplier(transaction.getType()));
        BigDecimal amount = transaction.getAmount().multiply(transactionMultiplier);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public List<TransactionDto> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto findById(Integer id) {
        return transactionRepository.findById(id)
                .map(TransactionDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No transaction was found with the ID: " + id));
    }

    @Override
    public void delete(Integer id) {
        transactionRepository.deleteById(id);
    }

    private int getTransactionMultiplier(TransactionType type) {
        return TransactionType.TRANSFERT == type ? -1 : 1;
    }
}
