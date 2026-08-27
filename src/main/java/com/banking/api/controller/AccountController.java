package com.banking.api.controller;

import com.banking.api.dto.CreateAccountRequest;
import com.banking.model.Account;
import com.banking.service.BankService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.api.dto.MoneyRequest;

import com.banking.service.TransactionService;
import com.banking.model.Transaction;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final BankService bankService;
    private final TransactionService transactionService;

    public AccountController(
            BankService bankService,
            TransactionService transactionService) {

        this.bankService = bankService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        Account account =
                bankService.openAccount(
                        request.getAccountType(),
                        request.getAccountNumber(),
                        request.getCustomerId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody MoneyRequest request) {

        bankService.deposit(
                accountNumber,
                request.getAmount()
        );

        return ResponseEntity.ok(
                "Deposit successful"
        );
    }


    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody MoneyRequest request) {

        bankService.withdraw(
                accountNumber,
                request.getAmount()
        );

        return ResponseEntity.ok(
                "Withdrawal successful"
        );
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(
            @PathVariable String accountNumber) {

        Account account =
                bankService.getAccount(accountNumber);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable String accountNumber) {

        List<Transaction> transactions =
                transactionService.getTransactions(
                        accountNumber
                );

        return ResponseEntity.ok(transactions);
    }
}