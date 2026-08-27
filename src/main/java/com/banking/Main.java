package com.banking;

import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.Customer;

import com.banking.service.BankService;
import com.banking.service.TransactionService;

import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;

import com.banking.repository.JdbcBankingTransactionRepository;
import com.banking.repository.JdbcCustomerRepository;
import com.banking.repository.JdbcAccountRepository;

import com.banking.repository.TransactionRepository;
import com.banking.repository.JdbcTransactionRepository;

import com.banking.service.TransactionReportService;

public class Main {

    public static void main(String[] args) {



        CustomerRepository customerRepository =
                new JdbcCustomerRepository();

        AccountRepository accountRepository =
                new JdbcAccountRepository(
                        customerRepository
                );

        TransactionRepository transactionRepository =
                new JdbcTransactionRepository();

        JdbcBankingTransactionRepository
                bankingTransactionRepository =
                new JdbcBankingTransactionRepository();

        TransactionService transactionService =
                new TransactionService(
                        transactionRepository,
                        bankingTransactionRepository
                );


        BankService bankService =
                new BankService(
                        accountRepository,
                        customerRepository,
                        transactionService
                );

        Customer harsh =
                new Customer(
                        "C001",
                        "Harsh",
                        "harsh@example.com"
                );

        Customer rahul =
                new Customer(
                        "C002",
                        "Rahul",
                        "rahul@example.com"
                );

        bankService.registerCustomer(harsh);
        bankService.registerCustomer(rahul);

        Account savings =
                bankService.openAccount(
                        AccountType.SAVINGS,
                        "SA1001",
                        "C001"
                );

        Account current =
                bankService.openAccount(
                        AccountType.CURRENT,
                        "CA1001",
                        "C002"
                );

        bankService.deposit("SA1001", 5000);
        bankService.deposit("CA1001", 2000);

        bankService.withdraw("SA1001", 1000);

        bankService.transfer(
                "SA1001",
                "CA1001",
                1000
        );

        savings = bankService.getAccount("SA1001");
        current = bankService.getAccount("CA1001");

        System.out.println(
                "Harsh Balance: ₹" +
                        savings.getBalance()
        );

        System.out.println(
                "Rahul Balance: ₹" +
                        current.getBalance()
        );

        System.out.println("\n--- Harsh Transaction History ---");

        transactionService
                .getTransactions("SA1001")
                .forEach(System.out::println);

        var transactions =
                transactionService
                        .getTransactions("SA1001");

        TransactionReportService reportService =
                new TransactionReportService();

        System.out.println(
                "Total deposits: ₹" +
                        reportService.getTotalDeposits(
                                transactions
                        )
        );

        System.out.println(
                "Total withdrawals: ₹" +
                        reportService.getTotalWithdrawals(
                                transactions
                        )
        );

        System.out.println(
                "Total transfers: ₹" +
                        reportService.getTotalTransfers(
                                transactions
                        )
        );

        System.out.println(
                "Transaction counts: " +
                        reportService.countTransactionsByType(
                                transactions
                        )
        );
    }
}