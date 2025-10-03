package org.yascode.banking.services;

import org.yascode.banking.dto.AuthenticationRequest;
import org.yascode.banking.dto.AuthenticationResponse;
import org.yascode.banking.dto.LightUserDto;
import org.yascode.banking.dto.UserDto;

public interface UserService extends AbstractService<UserDto> {

  Integer validateAccount(Integer id);

  Integer invalidateAccount(Integer id);

  AuthenticationResponse register(UserDto user);

  AuthenticationResponse authenticate(AuthenticationRequest request);

  Integer update(LightUserDto userDto);
}
