package ru.otus.bank.dao;

import ru.otus.bank.entity.Agreement;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class AgreementDao {

    HashMap<Long, Agreement> agreementMap = new HashMap<>();

    AtomicLong id = new AtomicLong(0);


    public Agreement addAgreement(String name) {
        Agreement agreement = new Agreement();
        agreement.setName(name);
        agreement.setId(id.incrementAndGet());

        agreementMap.put(agreement.getId(), agreement);
        return agreement;
    }

    public Optional<Agreement> findByName(String name) {
        return agreementMap.values().stream().filter(agreement -> agreement.getName().equals(name)).findFirst();
    }

    public Agreement save(Agreement agreement) {
        if (agreement.getId() == null || agreement.getId() == 0) {
            agreement.setId(id.incrementAndGet());
        }
        agreementMap.put(agreement.getId(), agreement);
        return agreement;
    }
}
