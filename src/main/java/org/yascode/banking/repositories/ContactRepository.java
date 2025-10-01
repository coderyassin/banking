package org.yascode.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.banking.models.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

  List<Contact> findAllByUserId(Integer userId);
}
