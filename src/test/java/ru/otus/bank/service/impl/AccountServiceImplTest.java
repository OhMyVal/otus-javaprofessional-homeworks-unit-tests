package ru.otus.bank.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.dao.AccountDao;
import ru.otus.bank.entity.Account;

import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testTransfer() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        assertEquals(new BigDecimal(90), sourceAccount.getAmount());
        assertEquals(new BigDecimal(20), destinationAccount.getAmount());
    }

    @Test
    public void testSourceNotFound() {
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));
            }
        });
        assertEquals("No source account", result.getLocalizedMessage());
    }


    @Test
    public void testTransferWithVerify() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));
        destinationAccount.setId(2L);

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        ArgumentMatcher<Account> sourceMatcher =
                argument -> argument.getId().equals(1L) && argument.getAmount().equals(new BigDecimal(90));

        ArgumentMatcher<Account> destinationMatcher =
                argument -> argument.getId().equals(2L) && argument.getAmount().equals(new BigDecimal(20));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        verify(accountDao).save(argThat(sourceMatcher));
        verify(accountDao).save(argThat(destinationMatcher));
    }

    @Test
    public void testAddAccount() {

        Agreement agreement = new Agreement();
        agreement.setId(10L);

        Account account = new Account();
        String number = "accountNumber";
        Integer type = 35;
        BigDecimal amount = new BigDecimal(100);

        account.setAgreementId(agreement.getId());
        account.setNumber(number);
        account.setType(type);
        account.setAmount(amount);

        ArgumentCaptor<Account> savedAccountsCaptor = ArgumentCaptor.captor();

        Mockito.when(accountDao.save(savedAccountsCaptor.capture())).thenReturn(account);

        Account result = accountServiceImpl.addAccount(agreement, number, type, amount);

        Assertions.assertEquals(10L, result.getAgreementId());
        Assertions.assertEquals("accountNumber", result.getNumber());
        Assertions.assertEquals(35, result.getType());
        Assertions.assertEquals(new BigDecimal(100), result.getAmount());
    }


    @ParameterizedTest
    @CsvSource({"1, 100, 10, true", "2, 10, 100, false", "4, 10, 0, false", "1, 10, -1, false"})
    public void testChargeTrueOrFalse(String sourceId, String sourceSum, String chargeSum, String expectedResult) {
        Long sourceAccountId = Long.parseLong(sourceId);
        BigDecimal sourceAmount = new BigDecimal(sourceSum);
        BigDecimal chargeAmount = new BigDecimal(chargeSum);
        Boolean expected = Boolean.parseBoolean(expectedResult);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(sourceAmount);
        sourceAccount.setId(sourceAccountId);

        when(accountDao.findById(eq(sourceAccountId))).thenReturn(Optional.of(sourceAccount));

        boolean result = accountServiceImpl.charge(sourceAccount.getId(), chargeAmount);
        assertEquals(expected, result);
    }

    @Test
    public void testChargeSourceNotFound() {

        when(accountDao.findById(any())).thenReturn(Optional.empty());
        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                accountServiceImpl.charge(1L, new BigDecimal(10));
            }
        });
        assertEquals("No source account", result.getLocalizedMessage());
    }

}
