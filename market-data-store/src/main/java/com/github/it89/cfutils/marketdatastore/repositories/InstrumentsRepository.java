package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InstrumentsRepository extends CrudRepository<InstrumentEntity, String> {
    List<InstrumentEntity> findAllByFigiIn(Set<String> figiSet);
    List<InstrumentEntity> findAllByIsinIn(Set<String> isinSet);
    Optional<InstrumentEntity> findFirstByFigi(String figi);
    Optional<InstrumentEntity> findFirstByIsinAndCurrency(String isin, Currency currency);
}
