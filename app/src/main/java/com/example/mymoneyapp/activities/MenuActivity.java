package com.example.mymoneyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;
import com.example.mymoneyapp.common.Constants;
import com.example.mymoneyapp.dialogs.DepositDialog;
import com.example.mymoneyapp.dialogs.TransactionDialog;
import com.example.mymoneyapp.dialogs.WithdrawDialog;

public class MenuActivity extends AppCompatActivity {

    private ImageButton transactionBtn;
    private ImageButton withdrawBtn;
    private ImageButton depositBtn;
    private ImageButton logutBtn;
    private int userAccountNumber;
    private TextView currentBalanceTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_activity);
        userAccountNumber = Integer.parseInt(getIntent().getStringExtra(Constants.ACCOUNT_NUMBER));
        currentBalanceTxt = findViewById(R.id.currentBalanceTxt);
        updateBalance();

        transactionBtn = findViewById(R.id.makeTransactionButton);
        withdrawBtn = findViewById(R.id.withDrawButton);
        depositBtn = findViewById(R.id.depositButton);
        logutBtn = findViewById(R.id.logoutButton);

        transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionDialog exampleDialog = new TransactionDialog(userAccountNumber, (dialogInterface, i) -> updateBalance());
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }
        });

        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WithdrawDialog exampleDialog = new WithdrawDialog(userAccountNumber, (dialogInterface, i) -> updateBalance());
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }
        });

        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DepositDialog exampleDialog = new DepositDialog(userAccountNumber, (dialogInterface, i) -> updateBalance());
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }
        });

        logutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BankRestClient.logout();
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "In order to logout, press logout button", Toast.LENGTH_LONG).show();
    }

    public void updateBalance() {
        float currentBalance = getAccountsCurrentBalance();
        currentBalanceTxt.setText("Current balance is: " + String.valueOf(currentBalance) + "$");
    }

    private float getAccountsCurrentBalance() {
        return BankRestClient.getUsersCurrentBalance(userAccountNumber).floatValue();
    }
}
