package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.exceptions.InstrumentNotFoundException;
import com.github.it89.cfutils.marketdatastore.repositories.CandlesRepository;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandleValuesService {
    private final CandlesRepository candlesRepository;
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public SortedMap<Instant, BigDecimal> getValues(String figi) {
        InstrumentEntity instrumentEntity = instrumentsRepository.findFirstByFigi(figi)
                .orElseThrow(InstrumentNotFoundException::new);

        var map = candlesRepository.getAllByInstrument(instrumentEntity).stream()
                .collect(Collectors.toMap(CandleEntity::getOpenTime, CandleEntity::getOpen));

        return new TreeMap<>(map);
    }

    @Transactional
    public SortedMap<Instant, BigDecimal> getValues(Long instrumentId) {
        InstrumentEntity instrumentEntity = instrumentsRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFoundException::new);

        var map = candlesRepository.getAllByInstrument(instrumentEntity).stream()
                .collect(Collectors.toMap(CandleEntity::getOpenTime, CandleEntity::getOpen));

        return new TreeMap<>(map);
    }
}
