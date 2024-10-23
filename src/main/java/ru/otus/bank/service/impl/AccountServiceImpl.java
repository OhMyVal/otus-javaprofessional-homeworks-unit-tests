package ru.otus.bank.service.impl;

import ru.otus.bank.dao.AccountDao;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.AccountService;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account addAccount(Agreement agreement, String accountNumber, Integer type, BigDecimal amount) {
        Account account = new Account();
        account.setAgreementId(agreement.getId());
        account.setNumber(accountNumber);
        account.setType(type);
        account.setAmount(amount);

        return accountDao.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return iterableToList(accountDao.findAll());
    }

    private <T> List<T> iterableToList(Iterable<T> src) {
        ArrayList<T> result = new ArrayList<>();
        src.forEach(result::add);
        return result;
    }

    @Override
    public boolean charge(Long accountId, BigDecimal chargeAmount) {
        Account account= accountDao.findById(accountId)
                .orElseThrow(() -> new AccountException("No source account"));
        account.setAmount(account.getAmount().subtract(chargeAmount));
        accountDao.save(account);
        return true;
    }

    public List<Account> getAccounts(Agreement agreement) {
        return iterableToList(accountDao.findByAgreementId(agreement.getId()));
    }

    public boolean makeTransfer(Long sourceAccountId, Long destinationAccountId, BigDecimal sum) {
        Account sourceAccount = accountDao.findById(sourceAccountId)
                .orElseThrow(() -> new AccountException("No source account"));
        Account destinationAccount = accountDao.findById(destinationAccountId)
                .orElseThrow(() -> new AccountException("No destination account"));

        sourceAccount.setAmount(sourceAccount.getAmount().subtract(sum));
        destinationAccount.setAmount(destinationAccount.getAmount().add(sum));

        if (sourceAccount.getAmount().compareTo(sum) < 0) {
            return false;
        }

        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        accountDao.save(sourceAccount);
        accountDao.save(destinationAccount);

        return true;
    }

}
