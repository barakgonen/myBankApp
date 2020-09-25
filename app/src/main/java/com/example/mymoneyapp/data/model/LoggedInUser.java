package com.example.mymoneyapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String idNumber;
    private String logInCode;

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