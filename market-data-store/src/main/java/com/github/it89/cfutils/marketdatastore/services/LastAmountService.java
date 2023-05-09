package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.converters.InstrumentConverter;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.models.AmountInfo;
import com.github.it89.cfutils.marketdatastore.models.BondAmount;
import com.github.it89.cfutils.marketdatastore.models.InstrumentAmountInfo;
import com.github.it89.cfutils.marketdatastore.models.InstrumentType;
import com.github.it89.cfutils.marketdatastore.models.MonetaryAmount;
import com.github.it89.cfutils.marketdatastore.models.PriceInfo;
import com.github.it89.cfutils.marketdatastore.repositories.BondAciRepository;
import com.github.it89.cfutils.marketdatastore.repositories.BondNominalRepository;
import com.github.it89.cfutils.marketdatastore.repositories.LastPriceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LastAmountService {
    private final LastPriceRepository lastPriceRepository;
    private final BondNominalRepository bondNominalRepository;
    private final BondAciRepository bondAciRepository;

    @Transactional
    public InstrumentAmountInfo getLastAmountByIsin(String isin) {
        InstrumentAmountInfo instrumentAmountInfo = new InstrumentAmountInfo();
        updatePriceInfo(instrumentAmountInfo, isin);
        updateBondAmount(instrumentAmountInfo, isin);
        updateAmountInfo(instrumentAmountInfo);
        return instrumentAmountInfo;
    }

    private void updatePriceInfo(InstrumentAmountInfo instrumentAmountInfo, String isin) {
        var lastPriceOptional = lastPriceRepository.findByLastByIsin(isin);
        if (lastPriceOptional.isPresent()) {
            var lastPriceEntity = lastPriceOptional.get();
            updateInstrument(instrumentAmountInfo, lastPriceEntity.getInstrument());

            PriceInfo priceInfo = new PriceInfo();
            priceInfo.setPrice(lastPriceEntity.getPrice());
            priceInfo.setTime(lastPriceEntity.getTime());
            instrumentAmountInfo.setPriceInfo(priceInfo);
        }
    }

    private void updateBondAmount(InstrumentAmountInfo instrumentAmountInfo, String isin) {
        BondAmount bondAmount = new BondAmount();

        var bondNominalOptional = bondNominalRepository.findByLastByIsin(isin);
        if (bondNominalOptional.isPresent()) {
            var bondNominalEntity = bondNominalOptional.get();
            updateInstrument(instrumentAmountInfo, bondNominalEntity.getInstrument());
            bondAmount.setNominalAmount(new AmountInfo(
                    new MonetaryAmount(bondNominalEntity.getNominalValue(), bondNominalEntity.getCurrency()),
                    bondNominalEntity.getTime()));
        }

        var bondAciOptional = bondAciRepository.findByLastByIsin(isin);
        if (bondAciOptional.isPresent()) {
            var bondAciEntity = bondAciOptional.get();
            updateInstrument(instrumentAmountInfo, bondAciEntity.getInstrument());
            bondAmount.setAciAmount(new AmountInfo(
                    new MonetaryAmount(bondAciEntity.getAciValue(), bondAciEntity.getCurrency()),
                    bondAciEntity.getTime()));
        }

        if (bondAmount.getNominalAmount() != null || bondAmount.getAciAmount() != null) {
            instrumentAmountInfo.setBondAmount(bondAmount);
        }
    }

    private void updateInstrument(InstrumentAmountInfo instrumentAmountInfo, InstrumentEntity instrumentEntity) {
        if (instrumentAmountInfo.getInstrument() == null) {
            instrumentAmountInfo.setInstrument(InstrumentConverter.entityToDto(instrumentEntity));
        }
    }

    private void updateAmountInfo(InstrumentAmountInfo instrumentAmountInfo) {
        if (instrumentAmountInfo.getPriceInfo() != null) {
            PriceInfo priceInfo = instrumentAmountInfo.getPriceInfo();

            if (instrumentAmountInfo.getInstrument().getType() == InstrumentType.BOND) {
                if (instrumentAmountInfo.getBondAmount() != null
                        && instrumentAmountInfo.getBondAmount().getNominalAmount() != null) {
                    AmountInfo nominalAmount = instrumentAmountInfo.getBondAmount().getNominalAmount();

                    BigDecimal value = priceInfo.getPrice()
                            .multiply(nominalAmount.getAmount().getAmount())
                            .divide(BigDecimal.valueOf(100), MathContext.UNLIMITED);

                    instrumentAmountInfo.setAmountInfo(new AmountInfo(
                            new MonetaryAmount(
                                    value,
                                    nominalAmount.getAmount().getCurrency()),
                            (Instant) ObjectUtils.max(priceInfo.getTime(), nominalAmount.getTime())));
                }
            } else {
                instrumentAmountInfo.setAmountInfo(new AmountInfo(
                        new MonetaryAmount(
                                priceInfo.getPrice(),
                                instrumentAmountInfo.getInstrument().getCurrency()),
                        priceInfo.getTime()));
            }
        }
    }
}
