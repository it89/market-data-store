package com.github.it89.cfutils.tcs.client.tcs;

import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import com.github.it89.cfutils.tcs.client.store.dto.InstrumentType;
import lombok.experimental.UtilityClass;
import ru.tinkoff.piapi.contract.v1.Currency;

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
}
