package com.example.mymoneyapp.data.model;

public class UserCredentials {

    private Integer accountNumber;
    private String pinCode;

    public UserCredentials(Integer accountNumber, String pinCode) {
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
    }

    public UserCredentials() {
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
