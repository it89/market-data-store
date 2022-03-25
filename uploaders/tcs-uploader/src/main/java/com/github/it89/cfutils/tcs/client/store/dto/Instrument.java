package com.github.it89.cfutils.tcs.client.store.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Currency;

@Data
@Builder
public class Instrument {
    private String figi;
    private String isin;
    private String ticker;
    private String name;
    private Currency currency;
}
