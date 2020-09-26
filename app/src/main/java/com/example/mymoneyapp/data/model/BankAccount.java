package com.example.mymoneyapp.data.model;

public class BankAccount {
    private Integer accountNumber;
    private float currentBalance;
    private String pinCode;
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
