package org.yascode.banking.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.yascode.banking.dto.AddressDto;
import org.yascode.banking.models.Address;
import org.yascode.banking.repositories.AddressRepository;
import org.yascode.banking.services.AddressService;
import org.yascode.banking.validators.ObjectsValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ObjectsValidator<AddressDto> validator;

    public AddressServiceImpl(AddressRepository addressRepository, ObjectsValidator<AddressDto> validator) {
        this.addressRepository = addressRepository;
        this.validator = validator;
    }

    @Override
    public Integer save(AddressDto addressDto) {
        validator.validate(addressDto);
        Address address = AddressDto.toEntity(addressDto);
        return addressRepository.save(address).getId();
    }

    @Override
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto findById(Integer id) {
        return addressRepository.findById(id)
                .map(AddressDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No address found with the ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        addressRepository.deleteById(id);
    }
}
