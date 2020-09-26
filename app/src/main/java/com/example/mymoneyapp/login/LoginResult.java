package com.example.mymoneyapp.login;

import androidx.annotation.Nullable;

import com.example.mymoneyapp.data.Result;
import com.example.mymoneyapp.data.model.BankAccount;

import static com.example.mymoneyapp.common.Constants.SUCCESSFUL;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    private LoggedInUserView success;
    private boolean isLoggedInSuccessfuly;
    public LoginResult(String serverResponse, BankAccount usersAccount) {
        success = new LoggedInUserView(serverResponse, usersAccount);
        isLoggedInSuccessfuly = serverResponse.equals(SUCCESSFUL);
    }

    @Nullable
    public LoggedInUserView getBankAccountView() {
        return success;
    }

    public boolean isLoggedInSuccessfuly(){
        return isLoggedInSuccessfuly;
    }
}