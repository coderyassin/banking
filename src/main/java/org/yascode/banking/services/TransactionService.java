package org.yascode.banking.services;

import org.yascode.banking.dto.TransactionDto;

import java.util.List;

public interface TransactionService extends AbstractService<TransactionDto> {

  List<TransactionDto> findAllByUserId(Integer userId);
}
