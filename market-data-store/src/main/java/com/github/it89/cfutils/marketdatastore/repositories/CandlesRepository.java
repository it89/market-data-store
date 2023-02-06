package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Repository
public interface CandlesRepository extends CrudRepository<CandleEntity, Long> {
    List<CandleEntity> getAllByInstrumentAndDurationAndOpenTimeBetween(
            InstrumentEntity instrument, Duration duration, Instant from, Instant to);

    List<CandleEntity> getAllByInstrument(InstrumentEntity instrument);
}
