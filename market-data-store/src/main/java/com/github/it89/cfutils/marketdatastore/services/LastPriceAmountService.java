package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.BondNominalValueEntity;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.entities.LastPriceEntity;
import com.github.it89.cfutils.marketdatastore.models.AmountInfo;
import com.github.it89.cfutils.marketdatastore.models.InstrumentType;
import com.github.it89.cfutils.marketdatastore.models.MonetaryAmount;
import com.github.it89.cfutils.marketdatastore.repositories.BondNominalRepository;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import com.github.it89.cfutils.marketdatastore.repositories.LastPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LastPriceAmountService {
    private final LastPriceRepository lastPriceRepostiory;
    private final InstrumentsRepository instrumentsRepository;
    private final BondNominalRepository bondNominalRepository;

    @Transactional
    public Map<String, AmountInfo> getAmount(Set<String> figiSet) {
        Map<String, LastPriceEntity> figiLastPriceEntityMap = lastPriceRepostiory.getLastPrices(figiSet).stream()
                .collect(Collectors.toUnmodifiableMap(LastPriceEntity::getFigi, v -> v));

        Map<String, InstrumentEntity> figiInstrumentMap = instrumentsRepository.getAllByFigiIn(figiSet).stream()
                .collect(Collectors.toUnmodifiableMap(InstrumentEntity::getFigi, v -> v));

        Map<String, BondNominalValueEntity> figiBondNominalMap = bondNominalRepository.getLastNominalValues(figiSet).stream()
                .collect(Collectors.toUnmodifiableMap(BondNominalValueEntity::getFigi, v -> v));

        Map<String, AmountInfo> result = new HashMap<>();
        figiSet.forEach(it -> result.put(
                it,
                getMonetaryAmount(it, figiLastPriceEntityMap, figiInstrumentMap, figiBondNominalMap)));

        return result;
    }

    private AmountInfo getMonetaryAmount(
            String figi,
            Map<String, LastPriceEntity> figiLastPriceEntityMap,
            Map<String, InstrumentEntity> figiInstrumentMap,
            Map<String, BondNominalValueEntity> figiBondNominalMap) {
        if (!figiLastPriceEntityMap.containsKey(figi)) {
            return null;
        }
        if (!figiInstrumentMap.containsKey(figi)) {
            return null;
        }

        InstrumentEntity instrument = figiInstrumentMap.get(figi);
        if (InstrumentType.BOND.equals(instrument.getType())) {
            return getBondNominalAmount(figi, figiLastPriceEntityMap.get(figi), figiBondNominalMap);
        } else {
            LastPriceEntity price = figiLastPriceEntityMap.get(figi);

            return new AmountInfo(
                    new MonetaryAmount(price.getPrice(), instrument.getCurrency()),
                    price.getTime()
            );
        }
    }

    private AmountInfo getBondNominalAmount(String figi,
                                            LastPriceEntity lastPrice,
                                            Map<String, BondNominalValueEntity> figiBondNominalMap) {
        if (!figiBondNominalMap.containsKey(figi)) {
            return null;
        }
        BondNominalValueEntity bondNominalEntity = figiBondNominalMap.get(figi);
        BigDecimal value = lastPrice.getPrice()
                .multiply(bondNominalEntity.getNominalValue())
                .divide(BigDecimal.valueOf(100), MathContext.UNLIMITED);
        Currency currency = bondNominalEntity.getCurrency();
        return new AmountInfo(new MonetaryAmount(value, currency), lastPrice.getTime());
    }

}
