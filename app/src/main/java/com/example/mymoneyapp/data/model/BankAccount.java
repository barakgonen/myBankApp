package com.example.mymoneyapp.data.model;

public class BankAccount {
    private Integer accountNumber;
//    @Column(name = "account_number", nullable = false)

    //    @Column(name = "current_balance", nullable = false)
    private float currentBalance;
    //    @Column(name = "pin_code", nullable = false)
    private String pinCode;
    //    @Column(name = "is_disabled", nullable = false)
    private boolean isDisabled;

    private Integer wrongLoginRetriesInRow;


    public BankAccount() {
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(float currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public Integer getWrongLoginRetriesInRow() {
        return wrongLoginRetriesInRow;
    }

    public void setWrongLoginRetriesInRow(Integer wrongLoginRetriesInRow) {
        this.wrongLoginRetriesInRow = wrongLoginRetriesInRow;
    }
}
