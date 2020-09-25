package com.example.mymoneyapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String idNumber;
    private String logInCode;
    private Integer accountNumber;
//    @Column(name = "account_number", nullable = false)

    //    @Column(name = "current_balance", nullable = false)
    private float currentBalance;
    //    @Column(name = "pin_code", nullable = false)
    private String pinCode;
    //    @Column(name = "is_disabled", nullable = false)
    private boolean isDisabled;

    public LoggedInUser(String userId, String displayName) {
        this.idNumber = userId;
        this.logInCode = displayName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getLogInCode() {
        return logInCode;
    }
}