package com.example.mymoneyapp.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.data.LoginRepository;
import com.example.mymoneyapp.data.Result;
import com.example.mymoneyapp.data.model.BankAccount;
import com.example.mymoneyapp.data.model.UserCredentials;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(UserCredentials userCredentials) {
        Result<BankAccount> result = loginRepository.login(userCredentials);
        if (result instanceof Result.Success)
            loginResult.setValue(new LoginResult(result.getResponseFromServer(),
                    ((Result.Success<BankAccount>) result).getData()));
        else
            loginResult.setValue(new LoginResult(result.getResponseFromServer(), null));
    }

    public void loginDataChanged(String accountPin) {
        if (!isAccountPinValid(accountPin)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_pin));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isAccountPinValid(String accountPin) {
        return accountPin != null && accountPin.trim().length() > 5;
    }
}