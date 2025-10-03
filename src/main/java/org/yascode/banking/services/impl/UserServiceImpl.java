package org.yascode.banking.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yascode.banking.config.JwtUtils;
import org.yascode.banking.dto.*;
import org.yascode.banking.models.Account;
import org.yascode.banking.models.Role;
import org.yascode.banking.models.User;
import org.yascode.banking.repositories.RoleRepository;
import org.yascode.banking.repositories.UserRepository;
import org.yascode.banking.services.AccountService;
import org.yascode.banking.services.UserService;
import org.yascode.banking.validators.ObjectsValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final ObjectsValidator<UserDto> validator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authManager;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           AccountService accountService,
                           ObjectsValidator<UserDto> validator,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           AuthenticationManager authManager,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authManager = authManager;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Integer validateAccount(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

        if (user.getAccount() == null) {
            AccountDto account = AccountDto.builder()
                    .user(UserDto.fromEntity(user))
                    .build();
            var savedAccount = accountService.save(account);
            user.setAccount(
                    Account.builder()
                            .id(savedAccount)
                            .build()
            );
        }

        user.setEnabled(true);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Integer invalidateAccount(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

        user.setEnabled(false);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public AuthenticationResponse register(UserDto userDto) {
        validator.validate(userDto);
        User user = UserDto.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(
                findOrCreateRole(ROLE_USER)
        );
        var savedUser = userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("fullName", savedUser.getFirstName() + " " + savedUser.getLastName());
        String token = jwtUtils.generateToken(savedUser, claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final User user = userRepository.findByEmail(request.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("fullName", user.getFirstName() + " " + user.getLastName());
        final String token = jwtUtils.generateToken(user, claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public Integer update(LightUserDto userDto) {
        User user = LightUserDto.toEntity(userDto);
        return userRepository.save(user).getId();
    }

    @Override
    public Integer save(UserDto dto) {
        validator.validate(dto);
        User user = UserDto.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer id) {
        return userRepository.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No user was found with the provided ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    private Role findOrCreateRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElse(null);
        if (role == null) {
            return roleRepository.save(
                    Role.builder()
                            .name(roleName)
                            .build()
            );
        }
        return role;
    }
}
