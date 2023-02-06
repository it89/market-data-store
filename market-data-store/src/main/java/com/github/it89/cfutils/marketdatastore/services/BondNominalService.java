package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.BondNominalValueEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.models.BondInfo;
import com.github.it89.cfutils.marketdatastore.repositories.BondNominalRepository;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
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
public class BondNominalService {
    private final BondNominalRepository bondNominalRepository;
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public void upload(Map<String, BondInfo> figiBondInfoMap) {
        List<InstrumentEntity> instrumentEntities = instrumentsRepository.findAllByFigiIn(figiBondInfoMap.keySet());

        Set<Long> instrumentIds = instrumentEntities.stream()
                .map(InstrumentEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, BondNominalValueEntity> bondNominalEntityMap =
                bondNominalRepository.getLastNominalValues(instrumentIds).stream()
                        .collect(Collectors.toUnmodifiableMap(it -> it.getInstrument().getId(), v -> v));

        instrumentEntities.forEach(i -> upload(i, figiBondInfoMap.get(i.getFigi()), bondNominalEntityMap));
    }

    private void upload(InstrumentEntity instrumentEntity,
                        BondInfo bondInfo,
                        Map<Long, BondNominalValueEntity> bondNominalEntityMap) {
        if (bondNominalEntityMap.containsKey(instrumentEntity.getId())) {
            update(bondNominalEntityMap.get(instrumentEntity.getId()), bondInfo);
        } else {
            create(instrumentEntity, bondInfo);
        }
    }

    private void update(BondNominalValueEntity entity, BondInfo bondInfo) {
        if (bondInfo.getTime().isBefore(entity.getTime())) {
            log.warn("Last time={} for instrument id={} is before then current time={}",
                    bondInfo.getTime(), entity.getInstrument().getId(), entity.getTime());
            return;
        }

        if (bondInfo.getTime().equals(entity.getTime())) {
            entity.setNominalValue(bondInfo.getNominalValue());
            entity.setCurrency(bondInfo.getNominalCurrency());
            bondNominalRepository.save(entity);
        } else {
            if (bondInfo.getNominalValue().compareTo(entity.getNominalValue()) != 0
                    || !bondInfo.getNominalCurrency().equals(entity.getCurrency())) {
                create(entity.getInstrument(), bondInfo);
            }
        }
    }

    private void create(InstrumentEntity instrumentEntity, BondInfo bondInfo) {
        BondNominalValueEntity entity = new BondNominalValueEntity();
        entity.setInstrument(instrumentEntity);
        entity.setNominalValue(bondInfo.getNominalValue());
        entity.setCurrency(bondInfo.getNominalCurrency());
        entity.setTime(bondInfo.getTime());
        bondNominalRepository.save(entity);
    }
}
