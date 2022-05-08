package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import com.github.it89.cfutils.marketdatastore.repositories.CandlesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandleValuesService {
    private final CandlesRepository candlesRepository;

    public SortedMap<Instant, BigDecimal> getValues(String figi) {
        var map = candlesRepository.getAllByFigi(figi).stream()
                .collect(Collectors.toMap(CandleEntity::getOpenTime, CandleEntity::getOpen));

        return new TreeMap<>(map);
    }
}
