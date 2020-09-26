package com.example.mymoneyapp.data;

import android.util.Pair;

import com.example.mymoneyapp.api.BankRestClient;
import com.example.mymoneyapp.data.model.BankAccount;
import com.example.mymoneyapp.data.model.UserCredentials;

import static com.example.mymoneyapp.common.Constants.SUCCESSFUL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<BankAccount> login(UserCredentials userCredentials) {

        try {
            // TODO: handle loggedInUser authentication
            Pair<String, BankAccount> queriedAccountData =
                    BankRestClient.getUserDataForCredentials(userCredentials);
            if (isValidAccountData(queriedAccountData))
                return new Result.Success<>(queriedAccountData.second, queriedAccountData.first);
            else
                return new Result.Error(queriedAccountData.first);
        } catch (Exception e) {
            return new Result.Error("Error caught while logging in: " + e.getMessage());
        }
    }

    private boolean isValidAccountData(Pair<String, BankAccount> queriedAccount) {
        return queriedAccount.first.equals(SUCCESSFUL) &&
                (queriedAccount.second.getPinCode() != null &&
                        queriedAccount.second.getAccountNumber() != null);
    }
}