package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.BondNominalValueEntity;
import com.github.it89.cfutils.marketdatastore.models.BondInfo;
import com.github.it89.cfutils.marketdatastore.repositories.BondNominalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BondNominalService {
    private final BondNominalRepository bondNominalRepository;

    @Transactional
    public void upload(Map<String, BondInfo> figiBondInfoMap) {
        Map<String, BondNominalValueEntity> figiEntityMap = bondNominalRepository.getLastNominalValues(figiBondInfoMap.keySet()).stream()
                .collect(Collectors.toUnmodifiableMap(BondNominalValueEntity::getFigi, v -> v));

        figiBondInfoMap.forEach((k, v) -> upload(k, v, figiEntityMap));
    }

    private void upload(String figi, BondInfo bondInfo, Map<String, BondNominalValueEntity> figiEntityMap) {
        if (figiEntityMap.containsKey(figi)) {
            update(figiEntityMap.get(figi), bondInfo);
        } else {
            create(figi, bondInfo);
        }
    }

    private void update(BondNominalValueEntity entity, BondInfo bondInfo) {
        if (bondInfo.getTime().isBefore(entity.getTime())) {
            log.warn("Last time={} for figi={} is before then current time={}",
                    bondInfo.getTime(), entity.getFigi(), entity.getTime());
            return;
        }

        if (bondInfo.getTime().equals(entity.getTime())) {
            entity.setNominalValue(bondInfo.getNominalValue());
            entity.setCurrency(bondInfo.getNominalCurrency());
            bondNominalRepository.save(entity);
        } else {
            if (bondInfo.getNominalValue().compareTo(entity.getNominalValue()) != 0
                    || !bondInfo.getNominalCurrency().equals(entity.getCurrency())) {
                create(entity.getFigi(), bondInfo);
            }
        }
    }

    private void create(String figi, BondInfo bondInfo) {
        BondNominalValueEntity entity = new BondNominalValueEntity();
        entity.setFigi(figi);
        entity.setNominalValue(bondInfo.getNominalValue());
        entity.setCurrency(bondInfo.getNominalCurrency());
        entity.setTime(bondInfo.getTime());
        bondNominalRepository.save(entity);
    }
}
