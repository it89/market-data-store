package com.github.it89.cfutils.tcs.client.tcs;

import com.github.it89.cfutils.tcs.client.store.dto.BondInstrument;
import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import com.github.it89.cfutils.tcs.client.store.dto.InstrumentType;
import lombok.experimental.UtilityClass;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.contract.v1.Currency;
import ru.tinkoff.piapi.contract.v1.Etf;
import ru.tinkoff.piapi.contract.v1.Share;

@UtilityClass
public class InstrumentsConverter {
    public static Instrument currencyToInstrument(Currency currency) {
        return Instrument.builder()
                .figi(currency.getFigi())
                .isin(currency.getIsin())
                .ticker(currency.getTicker())
                .name(currency.getName())
                .currency(java.util.Currency.getInstance(currency.getCurrency().toUpperCase()))
                .type(InstrumentType.CURRENCY)
                .build();
    }

    public static Instrument shareToInstrument(Share share) {
        return Instrument.builder()
                .figi(share.getFigi())
                .isin(share.getIsin())
                .ticker(share.getTicker())
                .name(share.getName())
                .currency(java.util.Currency.getInstance(share.getCurrency().toUpperCase()))
                .type(InstrumentType.SHARE)
                .build();
    }

    public static BondInstrument bondToBondInstrument(Bond bond) {
        Instrument instrument = Instrument.builder()
                .figi(bond.getFigi())
                .isin(bond.getIsin())
                .ticker(bond.getTicker())
                .name(bond.getName())
                .currency(java.util.Currency.getInstance(bond.getCurrency().toUpperCase()))
                .type(InstrumentType.BOND)
                .build();

        return BondInstrument.builder()
                .instrument(instrument)
                .nominalValue(ConvertUtils.moneyValueToBigDecimal(bond.getNominal()))
                .nominalCurrency(java.util.Currency.getInstance(bond.getNominal().getCurrency().toUpperCase()))
                .aciValue(ConvertUtils.moneyValueToBigDecimal(bond.getAciValue()))
                .aciCurrency(java.util.Currency.getInstance(bond.getAciValue().getCurrency().toUpperCase()))
                .build();
    }

    public static Instrument etfToInstrument(Etf etf) {
        return Instrument.builder()
                .figi(etf.getFigi())
                .isin(etf.getIsin())
                .ticker(etf.getTicker())
                .name(etf.getName())
                .currency(java.util.Currency.getInstance(etf.getCurrency().toUpperCase()))
                .type(InstrumentType.ETF)
                .build();
    }
}
