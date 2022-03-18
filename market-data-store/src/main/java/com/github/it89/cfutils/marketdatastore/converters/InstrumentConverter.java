package com.github.it89.cfutils.marketdatastore.converters;

import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.models.Instrument;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InstrumentConverter {
    public static Instrument entityToDto(InstrumentEntity entity) {
        return Instrument.builder()
                .figi(entity.getFigi())
                .isin(entity.getIsin())
                .ticker(entity.getTicker())
                .name(entity.getName())
                .currency(entity.getCurrency())
                .build();
    }
}
