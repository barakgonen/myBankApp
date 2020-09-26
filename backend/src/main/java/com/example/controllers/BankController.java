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

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import static com.example.Constants.SUCCESSFUL;
import static com.example.Constants.TRANSACTION_STATUS;

@RestController
@RequestMapping("/api")
public class BankController {
    @Autowired
    private BankBl bankBl;

    @GetMapping("/login/")
    public javax.ws.rs.core.Response loginToUsersBankAccount(HttpSession session, @RequestBody UserCredentials userCredentials) {
        Pair<String, BankAccount> queriedBankAccount = bankBl.getBankAccount(userCredentials);
        if (queriedBankAccount.getFirst().equals(SUCCESSFUL) && session.isNew())
            session.setAttribute("isConnected", "true");
        return Response.status(Response.Status.OK).header("login", queriedBankAccount.getFirst()).entity(queriedBankAccount.getSecond()).build();
    }

    @GetMapping("/bank/balance/{accountNumber}")
    public float currentBalanceForAccount(HttpSession session, @PathVariable Integer accountNumber) {
        if (session.getAttribute("isConnected").equals("true") && !session.isNew())
            return bankBl.getAccountBalance(accountNumber);
        return Float.parseFloat(null);
    }

    @PostMapping("/deposit/{account}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public javax.ws.rs.core.Response depositToAccount(HttpSession session, @PathVariable Integer account, @PathVariable float amount) {
        if (session.getAttribute("isConnected").equals("true") && !session.isNew()) {
            if (bankBl.depositToUsersAccount(account, amount))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }

    @PostMapping("/withdraw/{account}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public javax.ws.rs.core.Response withDraw(HttpSession session, @PathVariable Integer account, @PathVariable float amount) {
        if (session.getAttribute("isConnected").equals("true") && !session.isNew()) {
            String withDrawStatus = bankBl.withDrawUserAccount(account, amount);
            return Response.status(Response.Status.OK).header(TRANSACTION_STATUS, withDrawStatus).build();
        }
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }

    @PostMapping("/transaction/{sourceAccount}/{amount}/{destinationAccount}")
    public javax.ws.rs.core.Response transaction(HttpSession session, @PathVariable Integer sourceAccount, @PathVariable float amount, @PathVariable Integer destinationAccount) {
        if (session.getAttribute("isConnected").equals("true") && !session.isNew()) {
            String transactionStatus =
                    bankBl.preformTransactionBetweenAccounts(sourceAccount, amount, destinationAccount);
            return Response.status(Response.Status.OK).header(TRANSACTION_STATUS, transactionStatus).build();
        }
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }

    @PostMapping("/logout/")
    public javax.ws.rs.core.Response logout(HttpSession session){
        session.removeAttribute("isConnected");
        return Response.status(Response.Status.OK).build();
    }
}