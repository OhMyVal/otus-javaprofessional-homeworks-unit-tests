package ru.otus.bank.service;

import ru.otus.bank.entity.Agreement;

import java.util.Optional;

public interface AgreementService {
    Agreement addAgreement(String name);

    Optional<Agreement> findByName(String name);
}
