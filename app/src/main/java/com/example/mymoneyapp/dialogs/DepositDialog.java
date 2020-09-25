package com.example.mymoneyapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;

import java.util.function.Function;


public class DepositDialog extends AppCompatDialogFragment {
    private EditText editTextAmount;
    private int usersAccountNumber;
    private DialogInterface.OnClickListener okListener;

    public DepositDialog(int accountNumber, DialogInterface.OnClickListener okListener) {
        usersAccountNumber = accountNumber;
        this.okListener = okListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_deposit_dialog, null);
        builder.setView(view)
                .setTitle("Deposit to your own account")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float amountToDeposit = Float.parseFloat(editTextAmount.getText().toString());
                        if (amountToDeposit > 0)
                            BankRestClient.deposit(usersAccountNumber, amountToDeposit);
                        else
                            Toast.makeText(getContext(), "Can't deposite negative amount", Toast.LENGTH_LONG).show();
                        okListener.onClick(dialogInterface, i);
                    }
                });
        editTextAmount = view.findViewById(R.id.editAmount);
        return builder.create();
    }
}