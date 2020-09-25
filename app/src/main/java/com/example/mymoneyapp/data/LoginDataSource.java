package com.example.mymoneyapp.data;

import com.example.mymoneyapp.api.BankRestClient;
import com.example.mymoneyapp.data.model.BankAccount;
import com.example.mymoneyapp.data.model.LoggedInUser;
import com.example.mymoneyapp.data.model.UserCredentials;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<BankAccount> login(UserCredentials userCredentials) {

        try {
            // TODO: handle loggedInUser authentication
            BankAccount fakeUser = BankRestClient.getUserDataForCredentials(userCredentials);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout(Integer accountNumber) {
        // TODO: revoke authentication
        BankRestClient.logout(accountNumber);
    }
}