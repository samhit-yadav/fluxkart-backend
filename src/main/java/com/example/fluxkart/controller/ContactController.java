package com.example.fluxkart.controller;

import com.example.fluxkart.dto.ContactRequest;
import com.example.fluxkart.dto.ContactResponseDTO;
import com.example.fluxkart.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("FluxKart API is running! Use POST /identify to identify contacts.");
    }

    @PostMapping("/identify")
    public ResponseEntity<ContactResponseDTO> identifyContact(@RequestBody ContactRequest request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        ContactResponseDTO responseDTO = contactService.identifyContact(email, phoneNumber);
        return ResponseEntity.ok(responseDTO);
    }
}