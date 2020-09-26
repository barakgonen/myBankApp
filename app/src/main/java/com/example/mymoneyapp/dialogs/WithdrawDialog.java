package com.example.mymoneyapp.dialogs;

import android.content.DialogInterface;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;

import static com.example.mymoneyapp.common.Constants.WITHDRAW_DIALOG_ERROR_MSG;
import static com.example.mymoneyapp.common.Constants.WITHDRAW_DIALOG_TITLE;


public class WithdrawDialog extends AbstractDialog {
    public WithdrawDialog(int accountNumber, DialogInterface.OnClickListener okListener) {
        super(WITHDRAW_DIALOG_TITLE,
                accountNumber,
                okListener,
                WITHDRAW_DIALOG_ERROR_MSG,
                R.layout.custom_withdraw_dialog,
                R.id.editAmount);
    }

    @Override
    protected void preformApiCall() {
        BankRestClient.withdraw(usersAccountNumber, amountFromSrcAccount);
    }
}
