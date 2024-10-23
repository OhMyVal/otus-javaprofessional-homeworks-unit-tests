package ru.otus.bank.dao;

import ru.otus.bank.entity.Account;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AccountDao {

    HashMap<Long, Account> accountMap = new HashMap<>();

    AtomicLong id = new AtomicLong(1);

    public Iterable<Account> findByAgreementId(Long agreementId) {
        return accountMap.values().stream()
                .filter(account -> account.getAgreementId().equals(agreementId))
                .collect(Collectors.toList());
    }

    public Optional<Account> findById(Long accountId) {
        if (!accountMap.containsKey(accountId)) {
            return Optional.empty();
        }
        return Optional.of(accountMap.get(accountId));
    }

    public Account save(Account account) {
        if (account.getId() == null || account.getId() == 0) {
            account.setId(id.incrementAndGet());
        }

        accountMap.put(account.getId(), account);
        return account;
    }

    public Iterable<Account> findAll() {
        return accountMap.values();
    }


}
