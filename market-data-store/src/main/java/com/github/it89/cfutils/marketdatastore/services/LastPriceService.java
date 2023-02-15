package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.entities.LastPriceEntity;
import com.github.it89.cfutils.marketdatastore.models.PriceInfo;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import com.github.it89.cfutils.marketdatastore.repositories.LastPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LastPriceService {
    private final LastPriceRepository lastPriceRepository;
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public void upload(Map<String, PriceInfo> figiLastPriceMap) {
        List<InstrumentEntity> instrumentEntities = instrumentsRepository.findAllByFigiIn(figiLastPriceMap.keySet());

        Set<Long> instrumentIds = instrumentEntities.stream()
                .map(InstrumentEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, LastPriceEntity> lastPriceEntityMap =
                lastPriceRepository.getLastPrices(instrumentIds).stream()
                        .collect(Collectors.toMap(p -> p.getInstrument().getId(), p -> p));

        instrumentEntities.forEach(i -> upload(
                i,
                figiLastPriceMap.get(i.getFigi()),
                lastPriceEntityMap.get(i.getId())));
    }

    private void upload(InstrumentEntity instrumentEntity,
                        PriceInfo priceInfo,
                        LastPriceEntity lastPriceEntity) {
        if (lastPriceEntity != null) {
            update(lastPriceEntity, priceInfo);
        } else {
            create(instrumentEntity, priceInfo);
        }
    }

    private void update(LastPriceEntity entity, PriceInfo lastPrice) {
        if (lastPrice.getTime().isBefore(entity.getTime())) {
            log.warn("Last time={} for instrument id={} is before then current time={}",
                    lastPrice.getTime(), entity.getInstrument().getId(), entity.getTime());
            return;
        }

        if (lastPrice.getTime().equals(entity.getTime())) {
            entity.setPrice(lastPrice.getPrice());
            lastPriceRepository.save(entity);
        } else {
            if (lastPrice.getPrice().compareTo(entity.getPrice()) != 0) {
                create(entity.getInstrument(), lastPrice);
            }
        }
    }

    private void create(InstrumentEntity instrumentEntity, PriceInfo lastPrice) {
        LastPriceEntity entity = new LastPriceEntity();
        entity.setInstrument(instrumentEntity);
        entity.setPrice(lastPrice.getPrice());
        entity.setTime(lastPrice.getTime());
        lastPriceRepository.save(entity);
    }
}
