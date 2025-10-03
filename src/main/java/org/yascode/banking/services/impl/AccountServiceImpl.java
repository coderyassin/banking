package org.yascode.banking.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;
import org.yascode.banking.dto.AccountDto;
import org.yascode.banking.exceptions.OperationNonPermittedException;
import org.yascode.banking.models.Account;
import org.yascode.banking.repositories.AccountRepository;
import org.yascode.banking.services.AccountService;
import org.yascode.banking.validators.ObjectsValidator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ObjectsValidator<AccountDto> validator;

    public AccountServiceImpl(AccountRepository accountRepository, ObjectsValidator<AccountDto> validator) {
        this.accountRepository = accountRepository;
        this.validator = validator;
    }

    @Override
    public Integer save(AccountDto accountDto) {
        validator.validate(accountDto);

        if(Objects.nonNull(accountDto.getId())) {
            throw new OperationNonPermittedException(
                    "Account cannot be updated",
                    "save account",
                    "Account",
                    "Update not permitted"
            );
        }

        Account account = AccountDto.toEntity(accountDto);
        boolean userHasAlreadyAnAccount = accountRepository.findByUserId(account.getUser().getId()).isPresent();
        if (userHasAlreadyAnAccount && account.getUser().isEnabled()) {
            throw new OperationNonPermittedException(
                    "the selected user has already an active account",
                    "Create account",
                    "Account service",
                    "Account creation"
            );
        }

        String iban = generateRandomIban();

        account.setIban(iban);

        return accountRepository.save(account).getId();
    }

    @Override
    public List<AccountDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto findById(Integer id) {
        return accountRepository.findById(id)
                .map(AccountDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No account was found with the ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

    private String generateRandomIban() {
        String iban = Iban  .random(CountryCode.MA).toFormattedString();

        boolean ibanExists = accountRepository.findByIban(iban).isPresent();

        if (ibanExists) {
            generateRandomIban();
        }

        return iban;
    }
}
