package com.example;

public class Constants {
    public final static int INVALID_ACCOUNT_NUMBER = -1;
    public final static int UNSUCCESSFUL_OPERATION = 0;
    public final static int SUCCESSFUL_OPERATION = 1;
    public final static int INVALID_DST_ACCOUNT = 2;
    public final static int NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT = 3;
    public final static int TRIED_TO_LOGIN_TO_DISABLED_ACCOUNT = 4;
    public final static int SET_ACCOUNT_TO_DISABLED = 5;

    public final static int NUMBER_OF_FALSE_LOGIN_TRIES_TO_DISABLE_BANK_ACCOUNT = 3;

    public final static String SUCCESSFUL = "Chosen operation ended successfully!";
    public final static String NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT = "Can't finish operation, because: There is not enough money in your account";
    public final static String INVALID_DESTINATION_ACCOUNT = "Transaction had failed, because: invalid destination account";
    public final static String TRANSACTION_STATUS = "TransactionStatus";
    public final static String DISABLED_ACCOUNT_LOGIN = "Your account has been disabled. Please contact system administrator";
    public final static String SET_ACCOUNT_TO_DISABLEDD = "Your account has been disabled since you have tried to login for: " + NUMBER_OF_FALSE_LOGIN_TRIES_TO_DISABLE_BANK_ACCOUNT + " times in a row. Sorry";
    public final static String LOGIN_FAILED = "Login has failed, please try again";
}
