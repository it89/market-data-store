package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentsRepository extends CrudRepository<InstrumentEntity, String> {

}
