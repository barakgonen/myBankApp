package com.example.mymoneyapp.dialogs;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoneyapp.R;

public class TransactionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);

        Toast.makeText(getApplicationContext(), "Transaction activity", Toast.LENGTH_LONG).show();
    }
}
