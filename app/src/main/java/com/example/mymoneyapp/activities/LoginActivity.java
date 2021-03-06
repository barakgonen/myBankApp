package com.example.mymoneyapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.common.Constants;
import com.example.mymoneyapp.data.model.UserCredentials;
import com.example.mymoneyapp.login.LoginFormState;
import com.example.mymoneyapp.login.LoginResult;
import com.example.mymoneyapp.login.LoginViewModel;
import com.example.mymoneyapp.login.LoginViewModelFactory;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.JdkLoggerFactory;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText bankAccountNumber = findViewById(R.id.accountNumber);
        final EditText accountsPinCode = findViewById(R.id.pinCode);
        final ImageButton loginButton = findViewById(R.id.logIn);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    bankAccountNumber.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    accountsPinCode.setError(getString(loginFormState.getPasswordError()));
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

                Toast.makeText(getApplicationContext(), loginResult.getBankAccountView().getServerResponseMsg(), Toast.LENGTH_LONG).show();

                setResult(Activity.RESULT_OK);

                if (loginResult.isLoggedInSuccessfuly()) {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    intent.putExtra(Constants.ACCOUNT_NUMBER, String.valueOf(loginResult.getBankAccountView().getUsersAccount().getAccountNumber()));
                    intent.putExtra(Constants.PIN_CODE, loginResult.getBankAccountView().getUsersAccount().getPinCode());
                    startActivity(intent);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore this event is raised when user tries to input character, before it appeared on screen
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This event called after value has been written it takes the whole text box
                loginViewModel.loginDataChanged(accountsPinCode.getText().toString());
            }
        };
        bankAccountNumber.addTextChangedListener(afterTextChangedListener);
        accountsPinCode.addTextChangedListener(afterTextChangedListener);
        accountsPinCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    UserCredentials userCredentials = new UserCredentials(Integer.parseInt(bankAccountNumber.getText().toString()),
                            accountsPinCode.getText().toString());
                    loginViewModel.login(userCredentials);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText accountNumberEditText = findViewById(R.id.accountNumber);
                Integer accountNumber = Integer.parseInt(accountNumberEditText.getText().toString());
                EditText pinCodeEditTest = findViewById(R.id.pinCode);
                String pinCode = pinCodeEditTest.getText().toString();

                loadingProgressBar.setVisibility(View.VISIBLE);

                UserCredentials userCredentials = new UserCredentials(accountNumber, pinCode);
                loginViewModel.login(userCredentials);
            }
        });
    }
}