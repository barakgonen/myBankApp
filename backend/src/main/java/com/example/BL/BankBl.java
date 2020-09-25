package com.example.BL;

import com.example.Constants;
import com.example.model.BankAccount;
import com.example.model.UserCredentials;
import com.example.repository.BankAccountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.Constants.INVALID_DST_ACCOUNT;
import static com.example.Constants.NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT;
import static com.example.Constants.SUCCESSFUL_OPERATION;

@Service
public class BankBl {
    @Autowired
    BankAccountRepo repo;

    /**
     * Get all bank accounts registered in the bank
     *
     * @return
     */
    public List<BankAccount> getAllAccounts() {
        return (List<BankAccount>) repo.findAll();
    }

    /**
     * Finding bank account by account number
     *
     * @param userCredentials user credantials - user's bank account number & pin code
     * @return optional of bank account
     */
    public Optional<BankAccount> getBankAccount(UserCredentials userCredentials) {
        Optional<BankAccount> bankAccount = repo.findById(userCredentials.getAccountNumber());
        if (bankAccount.get() != null && areUserCredentialsValid(userCredentials, bankAccount.get()))
            return bankAccount;

        // In this case we didn't find the account in DB, or user's credentials are wrong
        return null;
    }

    private boolean areUserCredentialsValid(UserCredentials userCredentials, BankAccount queriedBankAccount) {
        return userCredentials.getPinCode().equals(queriedBankAccount.getPinCode());
    }

    public boolean depositToUsersAccount(int usersAccountNumber, float amountToDeposit) {
        Optional<BankAccount> bankAccount = repo.findById(usersAccountNumber);

        // In case we couldn't query user's account from DB, return false and alerting user
        if (bankAccount.get() == null)
            return false;

        updateAccountsBalance(bankAccount.get(), amountToDeposit);

        // Updated successfully
        return true;
    }

    private void updateAccountsBalance(BankAccount bankAccount, float amountToDeposit) {
        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() + amountToDeposit);
        BankAccount savedEntity = repo.save(bankAccount);
        System.out.println(savedEntity.getCurrentBalance());
    }

    public float getAccountBalance(int accountNumber) {
        return repo.findById(accountNumber).get().getCurrentBalance();
    }

    public int withDrawUserAccount(int usersAccountNumber, float amountToDeposit) {
        Optional<BankAccount> bankAccount = repo.findById(usersAccountNumber);

        // In case we couldn't query user's account from DB, return false and alerting user
        if (bankAccount.get() == null || bankAccount.get().getCurrentBalance() < amountToDeposit)
            return NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT;

        updateAccountsBalance(bankAccount.get(), -1 * amountToDeposit);

        // Updated successfully
        return SUCCESSFUL_OPERATION;
    }

    public int preformTransactionBetweenAccounts(int srcAccountNum, float amount, int dstAccountNum) {
        try {
            Optional<BankAccount> sourceAccount = repo.findById(srcAccountNum);
            Optional<BankAccount> dstAccount = repo.findById(dstAccountNum);

            // Returning false in case one of the accounts does not exists,
            // or there is no enough money in source account
            if (sourceAccount.get() == null || dstAccount.get() == null)
                return INVALID_DST_ACCOUNT;
            else if (sourceAccount.get().getCurrentBalance() < amount)
                return NOT_ENOUGH_MONEY_IN_SRC_ACCOUNT;
            else {
                updateAccountsBalance(sourceAccount.get(), -1 * amount);
                updateAccountsBalance(dstAccount.get(), amount);
                return SUCCESSFUL_OPERATION;
            }
        }
        catch (IllegalArgumentException | NoSuchElementException exc){
            return INVALID_DST_ACCOUNT;
        }
    }
}
