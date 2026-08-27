package com.banking.api.controller;


import com.banking.api.dto.CreateCustomerRequest;
import com.banking.model.Customer;
import com.banking.service.BankService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final BankService bankService;

    public CustomerController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        Customer customer =
                new Customer(
                        request.getId(),
                        request.getName(),
                        request.getEmail()
                );

        bankService.registerCustomer(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Customer created successfully");
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(
            @PathVariable String customerId) {

        Customer customer =
                bankService.getCustomer(customerId);

        return ResponseEntity.ok(customer);
    }
}