package com.example.mymoneyapp.login;

import com.example.mymoneyapp.data.model.BankAccount;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String serverResponseMsg;
    private BankAccount usersAccount;

    public LoggedInUserView(String serverResponseMsg, BankAccount usersAccountData) {
        this.serverResponseMsg = serverResponseMsg;
        this.usersAccount = usersAccountData;
    }

    public String getServerResponseMsg() {
        return serverResponseMsg;
    }

    public BankAccount getUsersAccount() {
        return usersAccount;
    }
}