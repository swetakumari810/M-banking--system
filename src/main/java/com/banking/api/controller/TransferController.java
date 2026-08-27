package com.banking.api.controller;

import com.banking.api.dto.TransferRequest;
import com.banking.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final BankService bankService;

    public TransferController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    public ResponseEntity<String> transfer(
            @Valid @RequestBody TransferRequest request) {

        bankService.transfer(
                request.getFromAccount(),
                request.getToAccount(),
                request.getAmount()
        );

        return ResponseEntity.ok(
                "Transfer successful"
        );
    }
}