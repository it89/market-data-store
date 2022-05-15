package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.LastPriceEntity;
import com.github.it89.cfutils.marketdatastore.models.LastPrice;
import com.github.it89.cfutils.marketdatastore.repositories.LastPriceRepostiory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LastPriceService {
    private final LastPriceRepostiory lastPriceRepostiory;

    @Transactional
    public void upload(Map<String, LastPrice> figiLastPriceMap) {
        Map<String, LastPriceEntity> figiEntityMap = lastPriceRepostiory.getLastPrices(figiLastPriceMap.keySet()).stream()
                .collect(Collectors.toUnmodifiableMap(LastPriceEntity::getFigi, v -> v));

        figiLastPriceMap.forEach((k, v) -> upload(k, v, figiEntityMap));
    }

    private void upload(String figi, LastPrice lastPrice, Map<String, LastPriceEntity> figiEntityMap) {
        if (figiEntityMap.containsKey(figi)) {
            update(figiEntityMap.get(figi), lastPrice);
        } else {
            create(figi, lastPrice);
        }
    }

    private void update(LastPriceEntity entity, LastPrice lastPrice) {
        if (lastPrice.getTime().isBefore(entity.getTime())) {
            log.warn("Last time={} for figi={} is before then current time={}",
                    lastPrice.getTime(), entity.getFigi(), entity.getTime());
            return;
        }

        if (lastPrice.getTime().equals(entity.getTime())) {
            entity.setPrice(lastPrice.getPrice());
            lastPriceRepostiory.save(entity);
        } else {
            if (lastPrice.getPrice().compareTo(entity.getPrice()) != 0) {
                create(entity.getFigi(), lastPrice);
            }
        }
    }

    private void create(String figi, LastPrice lastPrice) {
        LastPriceEntity entity = new LastPriceEntity();
        entity.setFigi(figi);
        entity.setPrice(lastPrice.getPrice());
        entity.setTime(lastPrice.getTime());
        lastPriceRepostiory.save(entity);
    }
}
