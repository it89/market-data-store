package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.BondAciValuesEntity;
import com.github.it89.cfutils.marketdatastore.models.BondInfo;
import com.github.it89.cfutils.marketdatastore.repositories.BondAciRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BondAciService {
    private final BondAciRepository bondAciRepository;

    @Transactional
    public void upload(Map<String, BondInfo> figiBondInfoMap) {
        Map<String, BondAciValuesEntity> figiEntityMap = bondAciRepository.getLastAciValues(figiBondInfoMap.keySet()).stream()
                .collect(Collectors.toUnmodifiableMap(BondAciValuesEntity::getFigi, v -> v));

        figiBondInfoMap.forEach((k, v) -> upload(k, v, figiEntityMap));
    }

    private void upload(String figi, BondInfo bondInfo, Map<String, BondAciValuesEntity> figiEntityMap) {
        if (figiEntityMap.containsKey(figi)) {
            update(figiEntityMap.get(figi), bondInfo);
        } else {
            create(figi, bondInfo);
        }
    }

    private void update(BondAciValuesEntity entity, BondInfo bondInfo) {
        if (bondInfo.getTime().isBefore(entity.getTime())) {
            log.warn("Last time={} for figi={} is before then current time={}",
                    bondInfo.getTime(), entity.getFigi(), entity.getTime());
            return;
        }

        if (bondInfo.getTime().equals(entity.getTime())) {
            entity.setAciValue(bondInfo.getAciValue());
            entity.setCurrency(bondInfo.getAciCurrency());
            bondAciRepository.save(entity);
        } else {
            if (bondInfo.getAciValue().compareTo(entity.getAciValue()) != 0
                    || !bondInfo.getAciCurrency().equals(entity.getCurrency())) {
                create(entity.getFigi(), bondInfo);
            }
        }
    }

    private void create(String figi, BondInfo bondInfo) {
        BondAciValuesEntity entity = new BondAciValuesEntity();
        entity.setFigi(figi);
        entity.setAciValue(bondInfo.getAciValue());
        entity.setCurrency(bondInfo.getAciCurrency());
        entity.setTime(bondInfo.getTime());
        bondAciRepository.save(entity);
    }
}
