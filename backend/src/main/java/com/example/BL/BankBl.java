package com.example.BL;

import com.example.model.BankAccount;
import com.example.model.UserCredentials;
import com.example.repository.BankAccountRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.Constants.DISABLED_ACCOUNT_LOGIN;
import static com.example.Constants.INVALID_ACCOUNT_NUMBER;
import static com.example.Constants.INVALID_DESTINATION_ACCOUNT;
import static com.example.Constants.LOGIN_FAILED;
import static com.example.Constants.NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT;
import static com.example.Constants.NUMBER_OF_FALSE_LOGIN_TRIES_TO_DISABLE_BANK_ACCOUNT;
import static com.example.Constants.SET_ACCOUNT_TO_DISABLEDD;
import static com.example.Constants.SUCCESSFUL;

@Service
public class BankBl {
    @Autowired
    BankAccountRepo repo;
    private Logger logger = LoggerFactory.getLogger("BankBl");
    private static final BankAccount NON_EXITING_BANK_ACCOUNT = new BankAccount();

    /**
     * Finding bank account by account number, in case bank account was found, and pin code is wrong,
     * increasing number of failed login retries by 1. When number of failed login retries in a row is
     * larger than NUMBER_OF_FALSE_LOGIN_TRIES_TO_DISABLE_BANK_ACCOUNT, account set to disabled.
     * On successful login action, number of failed login retries is zeroed.
     * Disabled account cannot log in to the bank.
     *
     * @param userCredentials user credantials - user's bank account number & pin code
     * @return pair of msg to user and bank account
     */
    public Pair<String, BankAccount> getBankAccount(UserCredentials userCredentials) {
        try {
            Optional<BankAccount> bankAccount = repo.findById(userCredentials.getAccountNumber());
            BankAccount queriedBankAccount = bankAccount.get();
            if (areUserCredentialsValid(userCredentials, queriedBankAccount)) {
                if (!isAccountDisabled(queriedBankAccount)) {
                    resetNumberOfFailedLoginRetries(queriedBankAccount);
                    logger.info("login was successful! returning bank account data to user");
                    return Pair.of(SUCCESSFUL, queriedBankAccount);
                } else {
                    logger.error("Tried to log in to disabled account. Account number is: " + queriedBankAccount.getAccountNumber() + " ask system admin for more data");
                    return Pair.of(DISABLED_ACCOUNT_LOGIN, NON_EXITING_BANK_ACCOUNT);
                }
            } else {
                logger.error("Invalid pin-code, account found but received pincode was not as expected. acutal: "
                        + userCredentials.getPinCode() + " expected: " + bankAccount.get().getPinCode());
                increaseNumberOfFailedLoginRetries(queriedBankAccount);
                if (accountSetToDisabled(queriedBankAccount))
                    return Pair.of(SET_ACCOUNT_TO_DISABLEDD, NON_EXITING_BANK_ACCOUNT);
                else
                    return Pair.of(LOGIN_FAILED, NON_EXITING_BANK_ACCOUNT);
            }
        } catch (NoSuchElementException exc) {
            logger.error("Couldn't find any bank account for received credentials. " +
                    "Caught NoSuchElementException");
            // This case means user didn't provide correct account. The account does not registerd
            // In bank's DB. For security manners, we can't tell him something more specific, because
            // we might provide information about our clients.
            return Pair.of(LOGIN_FAILED, NON_EXITING_BANK_ACCOUNT);
        }
    }

    /**
     * In case of successful login attempt, reset number of failed login retries
     *
     * @param queriedBankAccount bank account queried from bank's DB
     */
    private void resetNumberOfFailedLoginRetries(BankAccount queriedBankAccount) {
        queriedBankAccount.setWrongLoginRetriesInRow(0);
        repo.save(queriedBankAccount);
        logger.info("<resetNumberOfFailedLoginRetries()> Reset number of failed login retries for account number = {}", queriedBankAccount.getAccountNumber());
    }

    /**
     * Check if the pin-code that user has typed is equal to the one stored in bank's DB
     *
     * @param userCredentials    user's credentials
     * @param queriedBankAccount queried bank account
     * @return true if expected pin code equal to actual
     */
    private boolean areUserCredentialsValid(UserCredentials userCredentials, BankAccount queriedBankAccount) {
        return userCredentials.getPinCode().equals(queriedBankAccount.getPinCode());
    }

    /**
     * Checking if user's account is disabled
     *
     * @param queriedBankAccount queried bank account
     * @return true if disabled
     */
    private boolean isAccountDisabled(BankAccount queriedBankAccount) {
        return queriedBankAccount.isDisabled();
    }

    /**
     * Returns true if bank account set to disabled
     *
     * @param queriedBankAccount queried bank account from DB
     * @return
     */
    private boolean accountSetToDisabled(BankAccount queriedBankAccount) {
        if (queriedBankAccount.getWrongLoginRetriesInRow() ==
                NUMBER_OF_FALSE_LOGIN_TRIES_TO_DISABLE_BANK_ACCOUNT) {
            logger.warn("disabling account number: {}", queriedBankAccount.getAccountNumber());
            queriedBankAccount.setDisabled(true);
            repo.save(queriedBankAccount);
            return true;
        }

        logger.debug("Account number: {}", queriedBankAccount.getAccountNumber(), "has not disabled yet");
        return false;
    }

    /**
     * Increasing number of failed login retries
     *
     * @param queriedBankAccount bank account queried from DB
     */
    private void increaseNumberOfFailedLoginRetries(BankAccount queriedBankAccount) {
        logger.info("increasing number of failed logins for account: {}", queriedBankAccount.getAccountNumber());
        queriedBankAccount.setWrongLoginRetriesInRow(queriedBankAccount.getWrongLoginRetriesInRow() + 1);
        repo.save(queriedBankAccount);
        logger.info("increased number of failed logins for account: {}", queriedBankAccount.getAccountNumber());
    }

    /**
     * Deposit amount of money to user's account
     *
     * @param usersAccountNumber user's account number
     * @param amountToDeposit    amount to deposit
     * @return flag indicates weather operation has completed successfully
     */
    public boolean depositToUsersAccount(int usersAccountNumber, float amountToDeposit) {
        Optional<BankAccount> bankAccount = repo.findById(usersAccountNumber);

        // In case we couldn't query user's account from DB, return false and alerting user
        if (bankAccount.get() == null) {
            logger.error("<depositToUsersAccount()> User's account was not found in DB");
            return false;
        }

        updateAccountsBalance(bankAccount.get(), amountToDeposit);

        logger.info("<depositToUsersAccount()> Deposit to user's account was successful!");

        return true;
    }

    /**
     * Update account's balance
     *
     * @param bankAccount     queried bank account
     * @param amountToDeposit amount to deposit
     */
    private void updateAccountsBalance(BankAccount bankAccount, float amountToDeposit) {
        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() + amountToDeposit);
        repo.save(bankAccount);
    }

    /**
     * Getting current account's balance
     *
     * @param accountNumber account number
     * @return current balance
     */
    public float getAccountBalance(int accountNumber) {
        return repo.findById(accountNumber).get().getCurrentBalance();
    }

    /**
     * Withdraw an amount of money from user's account
     *
     * @param usersAccountNumber user's account number
     * @param amountToDeposit    amount to deposit
     * @return integer value represents exit code from the function, which will be converted to
     * string for user message in application
     */
    public String withDrawUserAccount(int usersAccountNumber, float amountToDeposit) {
        Optional<BankAccount> bankAccount = repo.findById(usersAccountNumber);

        // In case we couldn't query user's account from DB, return false and alerting user
        if (bankAccount.get() == null || bankAccount.get().getCurrentBalance() < amountToDeposit)
            return NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT;

        updateAccountsBalance(bankAccount.get(), -1 * amountToDeposit);

        // Updated successfully
        return SUCCESSFUL;
    }

    /**
     * Performing transaction between 2 bank accounts
     *
     * @param srcAccountNum source account number
     * @param amount        amount to deposit to destination account
     * @param dstAccountNum destination account
     * @return string indicates msg to user for application
     */
    public String preformTransactionBetweenAccounts(int srcAccountNum, float amount, int dstAccountNum) {
        try {
            Optional<BankAccount> sourceAccount = repo.findById(srcAccountNum);
            Optional<BankAccount> dstAccount = repo.findById(dstAccountNum);

            // Returning false in case one of the accounts does not exists,
            // or there is no enough money in source account
            if (sourceAccount.get() == null || dstAccount.get() == null)
                return INVALID_DESTINATION_ACCOUNT;
            else if (sourceAccount.get().getCurrentBalance() < amount) {
                logger.error("<preformTransactionBetweenAccounts()> Transaction has failed because " +
                        "there is no enough money in user's account");
                return NOT_ENOUGH_MONEY_IN_USERS_ACCOUNT;
            } else {
                updateAccountsBalance(sourceAccount.get(), -1 * amount);
                updateAccountsBalance(dstAccount.get(), amount);
                logger.debug("<preformTransactionBetweenAccounts()> Transaction has completed successfully!");
                return SUCCESSFUL;
            }
        } catch (NoSuchElementException exc) {
            logger.error("<preformTransactionBetweenAccounts()> Couldn't find destination account for transaction");
            return INVALID_DESTINATION_ACCOUNT;
        }
    }
}
