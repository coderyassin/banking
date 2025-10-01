package org.yascode.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.banking.models.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
