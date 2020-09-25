package com.example.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountNumber;
//    @Column(name = "account_number", nullable = false)

//    @Column(name = "current_balance", nullable = false)
    private float currentBalance;
//    @Column(name = "pin_code", nullable = false)
    private String pinCode;
//    @Column(name = "is_disabled", nullable = false)
    private boolean isDisabled;

    public BankAccount(Integer accountNumber, float currentBalance, String pinCode, boolean isDisabled) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.pinCode = pinCode;
        this.isDisabled = isDisabled;
    }

    public BankAccount() {
    }

    @Id
    @GeneratedValue
    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }
    @Column
    public float getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(float currentBalance) {
        this.currentBalance = currentBalance;
    }
    @Column
    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
    @Column
    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
