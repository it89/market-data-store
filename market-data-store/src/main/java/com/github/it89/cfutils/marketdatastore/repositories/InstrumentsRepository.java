package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InstrumentsRepository extends CrudRepository<InstrumentEntity, String> {
    List<InstrumentEntity> getAllByFigiIn(Set<String> figiSet);
    List<InstrumentEntity> getAllByIsinIn(Set<String> isinSet);
}
