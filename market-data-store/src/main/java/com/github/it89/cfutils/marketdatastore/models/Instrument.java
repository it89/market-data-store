package com.github.it89.cfutils.marketdatastore.models;

import lombok.Builder;
import lombok.Data;

import java.util.Currency;

@Data
@Builder
public class Instrument {
    private Long id;
    private String figi;
    private String isin;
    private String ticker;
    private String name;
    private Currency currency;
    private InstrumentType type;
}
