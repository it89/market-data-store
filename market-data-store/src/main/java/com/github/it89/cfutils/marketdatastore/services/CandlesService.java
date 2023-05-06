package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.CandleEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.entities.SourceEntity;
import com.github.it89.cfutils.marketdatastore.exceptions.InstrumentNotFoundException;
import com.github.it89.cfutils.marketdatastore.models.Candle;
import com.github.it89.cfutils.marketdatastore.repositories.CandlesRepository;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandlesService {
    private final CandlesRepository candlesRepository;
    private final InstrumentsRepository instrumentsRepository;
    private final SourcesService sourcesService;

    @Transactional
    public void upload(String figi, List<Candle> candles, Duration duration, String source) {
        InstrumentEntity instrumentEntity = instrumentsRepository.findFirstByFigi(figi)
                .orElseThrow(InstrumentNotFoundException::new);

        upload(instrumentEntity, candles, duration, source);
    }

    @Transactional
    public void upload(Long instrumentId, List<Candle> candles, Duration duration, String source) {
        InstrumentEntity instrumentEntity = instrumentsRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFoundException::new);

        upload(instrumentEntity, candles, duration, source);
    }

    private void upload(InstrumentEntity instrumentEntity, List<Candle> candles, Duration duration, String source) {
        SourceEntity sourceEntity = sourcesService.register(source);

        Map<Instant, CandleEntity> entityMap = getEntityList(instrumentEntity, candles, duration).stream()
                .collect(Collectors.toMap(CandleEntity::getOpenTime, v -> v));

        List<CandleEntity> updatedEntities = candles.stream()
                .map(it -> updateEntity(instrumentEntity, it, entityMap, duration, sourceEntity))
                .collect(Collectors.toList());

        candlesRepository.saveAll(updatedEntities);
    }

    private List<CandleEntity> getEntityList(InstrumentEntity instrumentEntity,
                                             List<Candle> candles,
                                             Duration duration) {
        if (!CollectionUtils.isEmpty(candles)) {
            Instant openTimeFrom = candles.stream()
                    .map(Candle::getOpenTime)
                    .min(Instant::compareTo).orElse(null);
            Instant openTimeTo = candles.stream()
                    .map(Candle::getOpenTime)
                    .max(Instant::compareTo).orElse(null);

            return candlesRepository.getAllByInstrumentAndDurationAndOpenTimeBetween(
                    instrumentEntity, duration, openTimeFrom, openTimeTo);
        } else {
            log.info("No candles for {} (figi={})", instrumentEntity.getTicker(), instrumentEntity.getFigi());
            return List.of();
        }
    }

    private CandleEntity updateEntity(InstrumentEntity instrumentEntity,
                                      Candle candle,
                                      Map<Instant, CandleEntity> entityMap,
                                      Duration duration,
                                      SourceEntity sourceEntity) {
        CandleEntity entity = entityMap.getOrDefault(
                candle.getOpenTime(),
                createEntity(instrumentEntity, candle, duration));

        entity.setOpen(candle.getOpen());
        entity.setClose(candle.getClose());
        entity.setComplete(candle.isComplete());
        entity.setSource(sourceEntity);
        return entity;
    }

    private CandleEntity createEntity(InstrumentEntity instrumentEntity,
                                      Candle candle,
                                      Duration duration) {
        CandleEntity entity = new CandleEntity();
        entity.setOpenTime(candle.getOpenTime());
        entity.setInstrument(instrumentEntity);
        entity.setDuration(duration);
        return entity;
    }
}
