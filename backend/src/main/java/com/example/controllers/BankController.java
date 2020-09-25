package com.example.controllers;

import com.example.BL.BankBl;
import com.example.model.BankAccount;
import com.example.model.UserCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/api")
public class BankController {
    @Autowired
    private BankBl bankBl;

    @GetMapping("/bank/accounts")
    public List<BankAccount> getAllAccounts() {
        return (List<BankAccount>) bankBl.getAllAccounts();
    }

    @GetMapping("/bank/balance/{accountNumber}")
    public float currentBalanceForAccount(@PathVariable Integer accountNumber){
        return bankBl.getAccountBalance(accountNumber);
    }

    @GetMapping("/account/")
    public BankAccount getAccountData(@RequestBody UserCredentials userCredentials) {
        Optional<BankAccount> account = bankBl.getBankAccount(userCredentials);
        return account.get();
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
        if (bankBl.withDrawUserAccount(account, amount))
            return Response.ok().build();
        else
            return Response.serverError().build();
    }
}