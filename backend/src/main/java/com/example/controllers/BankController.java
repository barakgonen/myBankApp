package com.example.controllers;

import com.example.BL.BankBl;
import com.example.Constants;
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

import static com.example.Constants.INVALID_DESTINATION_ACCOUNT;
import static com.example.Constants.INVALID_DST_ACCOUNT;
import static com.example.Constants.NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT;
import static com.example.Constants.NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT;
import static com.example.Constants.SUCCESSFUL;
import static com.example.Constants.SUCCESSFUL_OPERATION;
import static com.example.Constants.TRANSACTION_STATUS;

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
        String withDrawStatus = "";

        switch (bankBl.withDrawUserAccount(account, amount)){
            case SUCCESSFUL_OPERATION:
                withDrawStatus = SUCCESSFUL;
                break;
            case NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT:
                withDrawStatus = NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT;
                break;
        }
        return Response.status(Response.Status.OK).header(TRANSACTION_STATUS, withDrawStatus).build();
    }

    @PostMapping("/transaction/{sourceAccount}/{amount}/{destinationAccount}")
    public javax.ws.rs.core.Response transaction(@PathVariable Integer sourceAccount, @PathVariable float amount, @PathVariable Integer destinationAccount) {
        String transactionStatus = "";
        switch (bankBl.preformTransactionBetweenAccounts(sourceAccount, amount, destinationAccount)){
            case SUCCESSFUL_OPERATION:
                transactionStatus = SUCCESSFUL;
                break;
            case INVALID_DST_ACCOUNT:
                transactionStatus = INVALID_DESTINATION_ACCOUNT;
                break;
            case NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT:
                transactionStatus = NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT;
                break;
        }

        return Response.status(Response.Status.OK).header(TRANSACTION_STATUS, transactionStatus).build();
    }
}