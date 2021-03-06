package com.example.mymoneyapp.dialogs;

import android.content.DialogInterface;

import com.example.mymoneyapp.R;
import com.example.mymoneyapp.api.BankRestClient;

import static com.example.mymoneyapp.common.Constants.DEPOSIT_DIALOG_ERROR_MSG;
import static com.example.mymoneyapp.common.Constants.DEPOSIT_DIALOG_TITLE;

public class DepositDialog extends AbstractDialog {
    public DepositDialog(int accountNumber, DialogInterface.OnClickListener okListener) {
        super(DEPOSIT_DIALOG_TITLE,
                accountNumber,
                okListener,
                DEPOSIT_DIALOG_ERROR_MSG,
                R.layout.custom_deposit_dialog,
                R.id.editAmount);
    }

    @Override
    protected String preformApiCall() {
        boolean response = BankRestClient.deposit(usersAccountNumber, amountFromSrcAccount);
        if (response)
            return "Your money deposited to your account as expected";
        else
            return "Problem accorded while depositing money, try again";
    }
}
