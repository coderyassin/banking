package org.yascode.banking.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.yascode.banking.dto.ContactDto;
import org.yascode.banking.models.Contact;
import org.yascode.banking.repositories.ContactRepository;
import org.yascode.banking.services.ContactService;
import org.yascode.banking.validators.ObjectsValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ObjectsValidator<ContactDto> validator;

    public ContactServiceImpl(ContactRepository contactRepository, ObjectsValidator<ContactDto> validator) {
        this.contactRepository = contactRepository;
        this.validator = validator;
    }

    @Override
    public List<ContactDto> findAllByUserId(Integer userId) {
        return contactRepository.findAllByUserId(userId)
                .stream()
                .map(ContactDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Integer save(ContactDto dto) {
        validator.validate(dto);
        Contact contact = ContactDto.toEntity(dto);
        return contactRepository.save(contact).getId();
    }

    @Override
    public List<ContactDto> findAll() {
        return contactRepository.findAll()
                .stream()
                .map(ContactDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ContactDto findById(Integer id) {
        return contactRepository.findById(id)
                .map(ContactDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No contact was found with the ID :" + id));
    }

    @Override
    public void delete(Integer id) {
        contactRepository.deleteById(id);
    }
}
