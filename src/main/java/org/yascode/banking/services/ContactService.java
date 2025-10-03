package org.yascode.banking.services;

import org.yascode.banking.dto.ContactDto;

import java.util.List;

public interface ContactService extends AbstractService<ContactDto> {

  List<ContactDto> findAllByUserId(Integer userId);
}
