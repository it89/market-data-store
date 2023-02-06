package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.exceptions.InstrumentNotFoundException;
import com.github.it89.cfutils.marketdatastore.models.Candle;
import com.github.it89.cfutils.marketdatastore.repositories.CandlesRepository;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
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
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public void upload(String figi, List<Candle> candles, Duration duration) {
        InstrumentEntity instrumentEntity = instrumentsRepository.findFirstByFigi(figi)
                .orElseThrow(InstrumentNotFoundException::new);
        Map<Instant, CandleEntity> entityMap = getEntityList(instrumentEntity, candles, duration).stream()
                .collect(Collectors.toMap(CandleEntity::getOpenTime, v -> v));

        List<CandleEntity> updatedEntities = candles.stream()
                .map(it -> updateEntity(instrumentEntity, it, entityMap, duration))
                .collect(Collectors.toList());

        candlesRepository.saveAll(updatedEntities);
    }

    private List<CandleEntity> getEntityList(InstrumentEntity instrumentEntity,
                                             List<Candle> candles,
                                             Duration duration) {
        Instant openTimeFrom = candles.stream()
                .map(Candle::getOpenTime)
                .min(Instant::compareTo).orElse(null);
        Instant openTimeTo = candles.stream()
                .map(Candle::getOpenTime)
                .max(Instant::compareTo).orElse(null);

        return candlesRepository.getAllByInstrumentAndDurationAndOpenTimeBetween(
                instrumentEntity, duration, openTimeFrom, openTimeTo);
    }

    private CandleEntity updateEntity(InstrumentEntity instrumentEntity,
                                      Candle candle,
                                      Map<Instant, CandleEntity> entityMap,
                                      Duration duration) {
        CandleEntity entity = entityMap.getOrDefault(
                candle.getOpenTime(),
                createEntity(instrumentEntity, candle, duration));

        entity.setOpen(candle.getOpen());
        entity.setClose(candle.getClose());
        entity.setComplete(candle.isComplete());
        return entity;
    }

    private CandleEntity createEntity(InstrumentEntity instrumentEntity, Candle candle, Duration duration) {
        CandleEntity entity = new CandleEntity();
        entity.setOpenTime(candle.getOpenTime());
        entity.setInstrument(instrumentEntity);
        entity.setDuration(duration);
        return entity;
    }
}
