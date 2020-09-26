package com.example.mymoneyapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;

import static com.example.mymoneyapp.common.Constants.CANCEL;
import static com.example.mymoneyapp.common.Constants.OK;

public abstract class AbstractDialog extends AppCompatDialogFragment {
    protected String dialogTitle;
    protected EditText editTextAmount;
    protected int usersAccountNumber;
    protected DialogInterface.OnClickListener okListener;
    protected String exceptionMsgToUser;
    protected int sourceLayout;
    protected int editTextId;
    protected float amountFromSrcAccount;

    protected AbstractDialog(String dialogTitle,
                             int usersAccountNumber,
                             DialogInterface.OnClickListener okListener,
                             String exceptionMsgToUser,
                             int sourceLayout,
                             int editTextId) {
        this.dialogTitle = dialogTitle;
        this.usersAccountNumber = usersAccountNumber;
        this.okListener = okListener;
        this.exceptionMsgToUser = exceptionMsgToUser;
        this.sourceLayout = sourceLayout;
        this.editTextId = editTextId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(sourceLayout, null);
        builder.setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        amountFromSrcAccount = Float.parseFloat(editTextAmount.getText().toString());
                        if (amountFromSrcAccount > 0)
                            preformApiCall();
                        else
                            Toast.makeText(getContext(), exceptionMsgToUser, Toast.LENGTH_LONG).show();
                        okListener.onClick(dialogInterface, i);
                    }
                });
        editTextAmount = view.findViewById(editTextId);
        initializeOtherViews(view);
        return builder.create();
    }

    protected abstract void preformApiCall();

    protected void initializeOtherViews(View v){
        // Empty funcs. most dialog does not need it
    }
}
