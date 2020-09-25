package com.example.mymoneyapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.common.Constants;
import com.example.mymoneyapp.data.model.UserCredentials;
import com.example.mymoneyapp.login.LoggedInUserView;
import com.example.mymoneyapp.login.LoginFormState;
import com.example.mymoneyapp.login.LoginResult;
import com.example.mymoneyapp.login.LoginViewModel;
import com.example.mymoneyapp.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText identificationNum = findViewById(R.id.accountNumber);
        final EditText loginCode = findViewById(R.id.pinCode);
        final Button loginButton = findViewById(R.id.logIn);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    identificationNum.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    loginCode.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(identificationNum.getText().toString(),
                        loginCode.getText().toString());
            }
        };
        identificationNum.addTextChangedListener(afterTextChangedListener);
        loginCode.addTextChangedListener(afterTextChangedListener);
        loginCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    UserCredentials userCredentials = new UserCredentials(Integer.parseInt(identificationNum.getText().toString()),
                            loginCode.getText().toString());
                    loginViewModel.login(userCredentials);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "maniac", Toast.LENGTH_LONG).show();

                EditText accountNumberEditText = findViewById(R.id.accountNumber);
                Integer accountNumber = Integer.parseInt(accountNumberEditText.getText().toString());
                EditText pinCodeEditTest = findViewById(R.id.pinCode);
                String pinCode = pinCodeEditTest.getText().toString();

                loadingProgressBar.setVisibility(View.VISIBLE);

                UserCredentials userCredentials = new UserCredentials(accountNumber, pinCode);
                loginViewModel.login(userCredentials);

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra(Constants.ACCOUNT_NUMBER, String.valueOf(accountNumber));
                intent.putExtra(Constants.PIN_CODE, pinCode);
                startActivity(intent);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}