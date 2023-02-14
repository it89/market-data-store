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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LastPriceAmountService {
    private final LastPriceRepository lastPriceRepository;
    private final InstrumentsRepository instrumentsRepository;
    private final BondNominalRepository bondNominalRepository;

    @Transactional
    public Map<String, AmountInfo> getAmount(Set<String> figiSet) {
        List<InstrumentEntity> instrumentEntities = instrumentsRepository.findAllByFigiIn(figiSet);

        Set<Long> instrumentIds = instrumentEntities.stream()
                .map(InstrumentEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, LastPriceEntity> lastPriceMap = lastPriceRepository.getLastPrices(instrumentIds).stream()
                .collect(Collectors.toMap(p -> p.getInstrument().getId(), p -> p));


        Map<Long, BondNominalValueEntity> bondNominalMap =
                bondNominalRepository.getLastNominalValues(instrumentIds).stream()
                        .collect(Collectors.toUnmodifiableMap(k -> k.getInstrument().getId(), v -> v));

        Map<String, AmountInfo> result = new HashMap<>();
        instrumentEntities.forEach(it -> result.put(
                it.getFigi(),
                getMonetaryAmount(it, lastPriceMap, bondNominalMap)));

        return result;
    }

    private AmountInfo getMonetaryAmount(
            InstrumentEntity instrumentEntity,
            Map<Long, LastPriceEntity> lastPriceEntityMap,
            Map<Long, BondNominalValueEntity> bondNominalMap) {
        if (!lastPriceEntityMap.containsKey(instrumentEntity.getId())) {
            return null;
        }

        if (InstrumentType.BOND.equals(instrumentEntity.getType())) {
            return getBondNominalAmount(instrumentEntity.getId(), lastPriceEntityMap.get(
                    instrumentEntity.getId()), bondNominalMap);
        } else {
            LastPriceEntity price = lastPriceEntityMap.get(instrumentEntity.getId());

            return new AmountInfo(
                    new MonetaryAmount(price.getPrice(), instrumentEntity.getCurrency()),
                    price.getTime()
            );
        }
    }

    private AmountInfo getBondNominalAmount(long instrumentId,
                                            LastPriceEntity lastPrice,
                                            Map<Long, BondNominalValueEntity> instrumentIdBondNominalMap) {
        if (!instrumentIdBondNominalMap.containsKey(instrumentId)) {
            return null;
        }
        BondNominalValueEntity bondNominalEntity = instrumentIdBondNominalMap.get(instrumentId);
        BigDecimal value = lastPrice.getPrice()
                .multiply(bondNominalEntity.getNominalValue())
                .divide(BigDecimal.valueOf(100), MathContext.UNLIMITED);
        Currency currency = bondNominalEntity.getCurrency();
        return new AmountInfo(new MonetaryAmount(value, currency), lastPrice.getTime());
    }

}
