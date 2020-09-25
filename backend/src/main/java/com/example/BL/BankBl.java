package com.example.BL;

import com.example.model.BankAccount;
import com.example.model.UserCredentials;
import com.example.repository.BankAccountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankBl {
    @Autowired
    BankAccountRepo repo;

    /**
     * Get all bank accounts registered in the bank
     * @return
     */
    public List<BankAccount> getAllAccounts() {
        return (List<BankAccount>) repo.findAll();
    }

    /**
     * Finding bank account by account number
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

    private boolean areUserCredentialsValid(UserCredentials userCredentials, BankAccount queriedBankAccount){
        return userCredentials.getPinCode().equals(queriedBankAccount.getPinCode());
    }

    public boolean depositToUsersAccount(int usersAccountNumber, float amountToDeposit){
        Optional<BankAccount> bankAccount = repo.findById(usersAccountNumber);

        // In case we couldn't query user's account from DB, return false and alerting user
        if (bankAccount.get() == null)
            return false;

        updateAccountsBalance(usersAccountNumber, amountToDeposit);

        // Updated successfully
        return true;
    }

    private void updateAccountsBalance(int usersAccountNumber, float amountToDeposit) {
        BankAccount usersAccount = repo.findById(usersAccountNumber).get();
        usersAccount.setCurrentBalance(usersAccount.getCurrentBalance() + amountToDeposit);
        BankAccount savedEntity = repo.save(usersAccount);
        System.out.println(savedEntity.getCurrentBalance());
    }

    public float getAccountBalance(int accountNumber){
        return repo.findById(accountNumber).get().getCurrentBalance();
    }

    public boolean withDrawUserAccount(int usersAccountNumber, float amountToDeposit){
        Optional<BankAccount> bankAccount = repo.findById(usersAccountNumber);

        // In case we couldn't query user's account from DB, return false and alerting user
        if (bankAccount.get() == null || bankAccount.get().getCurrentBalance() < amountToDeposit)
            return false;

        updateAccountsBalance(usersAccountNumber, -1 * amountToDeposit);

        // Updated successfully
        return true;
    }
}
