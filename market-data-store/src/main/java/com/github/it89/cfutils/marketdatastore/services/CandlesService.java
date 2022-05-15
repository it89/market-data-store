package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import com.github.it89.cfutils.marketdatastore.models.Candle;
import com.github.it89.cfutils.marketdatastore.repositories.CandlesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandlesService {
    private final CandlesRepository candlesRepository;

    @Transactional
    public void upload(String figi, List<Candle> candles, Duration duration) {
        Map<Instant, CandleEntity> entityMap = getEntityList(figi, candles, duration).stream()
                .collect(Collectors.toMap(CandleEntity::getOpenTime, v -> v));

        List<CandleEntity> updatedEntities = candles.stream()
                .map(it -> updateEntity(figi, it, entityMap, duration))
                .collect(Collectors.toList());

        candlesRepository.saveAll(updatedEntities);
    }

    private List<CandleEntity> getEntityList(String figi, List<Candle> candles, Duration duration) {
        Instant openTimeFrom = candles.stream()
                .map(Candle::getOpenTime)
                .min(Instant::compareTo).orElse(null);
        Instant openTimeTo = candles.stream()
                .map(Candle::getOpenTime)
                .max(Instant::compareTo).orElse(null);

        return candlesRepository.getAllByFigiAndDurationAndOpenTimeBetween(figi, duration, openTimeFrom, openTimeTo);
    }

    private CandleEntity updateEntity(String figi, Candle candle,
                                      Map<Instant, CandleEntity> entityMap,
                                      Duration duration) {
        CandleEntity entity = entityMap.getOrDefault(candle.getOpenTime(), createEntity(figi, candle, duration));
        entity.setOpen(candle.getOpen());
        entity.setClose(candle.getClose());
        entity.setComplete(candle.isComplete());
        return entity;
    }

    private CandleEntity createEntity(String figi, Candle candle, Duration duration) {
        CandleEntity entity = new CandleEntity();
        entity.setOpenTime(candle.getOpenTime());
        entity.setFigi(figi);
        entity.setDuration(duration);
        return entity;
    }
}
