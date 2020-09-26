package com.example.mymoneyapp.dialogs;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;

import static com.example.mymoneyapp.common.Constants.TRANSACTION_DIALOG_ERROR_MSG;
import static com.example.mymoneyapp.common.Constants.TRANSACTION_DIALOG_TITLE;


public class TransactionDialog extends AbstractDialog {
    private EditText destinationAccountTxt;

    public TransactionDialog(int accountNumber, DialogInterface.OnClickListener okListener) {
        super(TRANSACTION_DIALOG_TITLE,
                accountNumber,
                okListener,
                TRANSACTION_DIALOG_ERROR_MSG,
                R.layout.custom_transaction_dialog,
                R.id.editAmountTxt);
    }

    @Override
    protected void initializeOtherViews(View v) {
        destinationAccountTxt = v.findViewById(R.id.destinationAccountTxt);
    }

    @Override
    protected void preformApiCall() {
        int destinationAccountNumber = Integer.parseInt(destinationAccountTxt.getText().toString());
        BankRestClient.executeTransactionToAnotherAccount(usersAccountNumber, amountFromSrcAccount, destinationAccountNumber);
    }
}
