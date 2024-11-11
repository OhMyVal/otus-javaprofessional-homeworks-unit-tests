package ru.otus.bank.service;

import ru.otus.bank.entity.Agreement;

import java.math.BigDecimal;


public interface PaymentProcessor {

    boolean makeTransfer(Agreement source, Agreement destination, int sourceType, int destinationType, BigDecimal amount);

    boolean makeTransferWithCommission(Agreement source, Agreement destination,
                                       int sourceType, int destinationType,
                                       BigDecimal amount,
                                       BigDecimal commissionPercent);
}

