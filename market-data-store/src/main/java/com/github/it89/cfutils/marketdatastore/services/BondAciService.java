package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.converters.InstrumentConverter;
import com.github.it89.cfutils.marketdatastore.entities.BondAciValuesEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.models.AmountInfo;
import com.github.it89.cfutils.marketdatastore.models.BondInfo;
import com.github.it89.cfutils.marketdatastore.models.Instrument;
import com.github.it89.cfutils.marketdatastore.models.MonetaryAmount;
import com.github.it89.cfutils.marketdatastore.repositories.BondAciRepository;
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
public class BondAciService {
    private final BondAciRepository bondAciRepository;
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public void upload(Map<String, BondInfo> figiBondInfoMap) {
        List<InstrumentEntity> instrumentEntities = instrumentsRepository.findAllByFigiIn(figiBondInfoMap.keySet());

        Set<Long> instrumentIds = instrumentEntities.stream()
                .map(InstrumentEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, BondAciValuesEntity> bondAciEntityMap = bondAciRepository.getLastAciValues(instrumentIds).stream()
                .collect(Collectors.toUnmodifiableMap(k -> k.getInstrument().getId(), v -> v));

        instrumentEntities.forEach(i -> upload(i, figiBondInfoMap.get(i.getFigi()), bondAciEntityMap));
    }

    @Transactional
    public Map<Instrument, AmountInfo> getAmount(Set<String> figiSet) {
        Set<Long> instrumentIds = instrumentsRepository.findAllByFigiIn(figiSet).stream()
                .map(InstrumentEntity::getId)
                .collect(Collectors.toSet());

        return bondAciRepository.getLastAciValues(instrumentIds).stream()
                .collect(Collectors.toUnmodifiableMap(
                                it -> InstrumentConverter.entityToDto(it.getInstrument()),
                                v -> new AmountInfo(new MonetaryAmount(v.getAciValue(), v.getCurrency()), v.getTime())
                        )
                );
    }

    private void upload(InstrumentEntity instrumentEntity,
                        BondInfo bondInfo, Map<Long,
            BondAciValuesEntity> bondAciEntityMap) {
        if (bondAciEntityMap.containsKey(instrumentEntity.getId())) {
            update(bondAciEntityMap.get(instrumentEntity.getId()), bondInfo);
        } else {
            create(instrumentEntity, bondInfo);
        }
    }

    private void update(BondAciValuesEntity entity, BondInfo bondInfo) {
        if (bondInfo.getTime().isBefore(entity.getTime())) {
            log.warn("Last time={} for instrument ID={} is before then current time={}",
                    bondInfo.getTime(), entity.getInstrument().getId(), entity.getTime());
            return;
        }

        if (bondInfo.getTime().equals(entity.getTime())) {
            entity.setAciValue(bondInfo.getAciValue());
            entity.setCurrency(bondInfo.getAciCurrency());
            bondAciRepository.save(entity);
        } else {
            if (bondInfo.getAciValue().compareTo(entity.getAciValue()) != 0
                    || !bondInfo.getAciCurrency().equals(entity.getCurrency())) {
                create(entity.getInstrument(), bondInfo);
            }
        }
    }

    private void create(InstrumentEntity instrumentEntity, BondInfo bondInfo) {
        BondAciValuesEntity entity = new BondAciValuesEntity();
        entity.setInstrument(instrumentEntity);
        entity.setAciValue(bondInfo.getAciValue());
        entity.setCurrency(bondInfo.getAciCurrency());
        entity.setTime(bondInfo.getTime());
        bondAciRepository.save(entity);
    }
}
