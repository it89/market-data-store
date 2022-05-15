package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.entities.LastPriceEntity;
import com.github.it89.cfutils.marketdatastore.models.InstrumentType;
import com.github.it89.cfutils.marketdatastore.models.MonetaryAmount;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import com.github.it89.cfutils.marketdatastore.repositories.LastPriceRepostiory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LastPriceAmountService {
    private final LastPriceRepostiory lastPriceRepostiory;
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public Map<String, MonetaryAmount> getAmount(Set<String> figiSet) {
        Map<String, LastPriceEntity> figiLastPriceEntityMap = lastPriceRepostiory.getLastPrices(figiSet).stream()
                .collect(Collectors.toUnmodifiableMap(LastPriceEntity::getFigi, v -> v));

        Map<String, InstrumentEntity> figiInstrumentMap = instrumentsRepository.getAllByFigiIn(figiSet).stream()
                .collect(Collectors.toUnmodifiableMap(InstrumentEntity::getFigi, v -> v));

        Map<String, MonetaryAmount> result = new HashMap<>();
        figiSet.forEach(it -> result.put(it, getMonetaryAmount(it, figiLastPriceEntityMap, figiInstrumentMap)));

        return result;
    }

    private MonetaryAmount getMonetaryAmount(
            String figi,
            Map<String, LastPriceEntity> figiLastPriceEntityMap,
            Map<String, InstrumentEntity> figiInstrumentMap) {
        if (!figiLastPriceEntityMap.containsKey(figi)) {
            return null;
        }
        if (!figiInstrumentMap.containsKey(figi)) {
            return null;
        }

        InstrumentEntity instrument = figiInstrumentMap.get(figi);
        if (InstrumentType.BOND.equals(instrument.getType())) {
            // TODO: implement
            return null;
        } else {
            return new MonetaryAmount(figiLastPriceEntityMap.get(figi).getPrice(), instrument.getCurrency());
        }

    }

}
