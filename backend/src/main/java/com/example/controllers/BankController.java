package com.example.controllers;

import com.example.BL.BankBl;
import com.example.model.BankAccount;
import com.example.model.UserCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

import static com.example.Constants.TRANSACTION_STATUS;

@RestController
@RequestMapping("/api")
public class BankController {
    @Autowired
    private BankBl bankBl;

    @GetMapping("/login/")
    public Pair<String, BankAccount> loginToUsersBankAccount(@RequestBody UserCredentials userCredentials) {
        Pair<String, BankAccount> queriedBankAccount = bankBl.getBankAccount(userCredentials);
        return queriedBankAccount;
    }

    @GetMapping("/bank/balance/{accountNumber}")
    public float currentBalanceForAccount(@PathVariable Integer accountNumber) {
        return bankBl.getAccountBalance(accountNumber);
    }

    @PostMapping("/deposit/{account}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public javax.ws.rs.core.Response depositToAccount(@PathVariable Integer account, @PathVariable float amount) {
        if (bankBl.depositToUsersAccount(account, amount))
            return Response.ok().build();
        else
            return Response.serverError().build();
    }

    @PostMapping("/withdraw/{account}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public javax.ws.rs.core.Response withDraw(@PathVariable Integer account, @PathVariable float amount) {
        String withDrawStatus = bankBl.withDrawUserAccount(account, amount);
        return Response.status(Response.Status.OK).header(TRANSACTION_STATUS, withDrawStatus).build();
    }

    @PostMapping("/transaction/{sourceAccount}/{amount}/{destinationAccount}")
    public javax.ws.rs.core.Response transaction(@PathVariable Integer sourceAccount, @PathVariable float amount, @PathVariable Integer destinationAccount) {
        String transactionStatus =
                bankBl.preformTransactionBetweenAccounts(sourceAccount, amount, destinationAccount);
        return Response.status(Response.Status.OK).header(TRANSACTION_STATUS, transactionStatus).build();
    }
}