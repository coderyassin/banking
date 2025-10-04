package org.yascode.banking.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yascode.banking.dto.ContactDto;
import org.yascode.banking.services.ContactService;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Integer> save(@RequestBody ContactDto contactDto) {
        return ResponseEntity.ok(service.save(contactDto));
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{contact-id}")
    public ResponseEntity<ContactDto> findById(
            @PathVariable("contact-id") Integer contactId
    ) {
        return ResponseEntity.ok(service.findById(contactId));
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<List<ContactDto>> findAllByUserId(
            @PathVariable("user-id") Integer userId
    ) {
        return ResponseEntity.ok(service.findAllByUserId(userId));
    }

    @DeleteMapping("/{contact-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("contact-id") Integer contactId
    ) {
        service.delete(contactId);
        return ResponseEntity.accepted().build();
    }

}
