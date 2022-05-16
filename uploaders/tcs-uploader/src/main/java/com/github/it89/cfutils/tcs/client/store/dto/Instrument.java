package com.github.it89.cfutils.tcs.client.store.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Currency;

@Value
@Builder
public class Instrument {
    String figi;
    String isin;
    String ticker;
    String name;
    Currency currency;
    InstrumentType type;
}
