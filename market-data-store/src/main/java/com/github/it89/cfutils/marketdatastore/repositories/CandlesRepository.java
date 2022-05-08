package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Repository
public interface CandlesRepository extends CrudRepository<CandleEntity, Long> {
    List<CandleEntity> getAllByFigiAndDurationAndOpenTimeBetween(
            String figi, Duration duration, Instant from, Instant to);
}
