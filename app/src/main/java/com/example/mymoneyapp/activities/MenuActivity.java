package com.example.mymoneyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;
import com.example.mymoneyapp.common.Constants;
import com.example.mymoneyapp.dialogs.DepositActivity;
import com.example.mymoneyapp.dialogs.TransactionActivity;
import com.example.mymoneyapp.dialogs.WithdrawActivity;

public class MenuActivity extends AppCompatActivity {

    private Button transactionBtn;
    private Button withdrawBtn;
    private Button depositBtn;
    private int userAccountNumber;
    private TextView currentBalanceTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        userAccountNumber = Integer.parseInt(getIntent().getStringExtra(Constants.ACCOUNT_NUMBER));
        currentBalanceTxt = findViewById(R.id.currentBalanceTxt);
        updateBalance();

        transactionBtn = findViewById(R.id.makeTransactionButton);
        withdrawBtn = findViewById(R.id.withDrawButton);
        depositBtn = findViewById(R.id.depositButton);

        transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
                startActivity(intent);
            }
        });

        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WithdrawActivity exampleDialog = new WithdrawActivity(userAccountNumber, (dialogInterface, i) -> updateBalance());
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }
        });

        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DepositActivity exampleDialog = new DepositActivity(userAccountNumber, (dialogInterface, i) -> updateBalance());
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }
        });
    }

    public void updateBalance() {
        float currentBalance = getAccountsCurrentBalance();
        currentBalanceTxt.setText(String.valueOf(currentBalance));
    }

    private float getAccountsCurrentBalance() {
        return BankRestClient.getUsersCurrentBalance(userAccountNumber).floatValue();
    }
}
