package com.github.it89.cfutils.marketdatastore.controllers.requests;

import lombok.Data;

import java.util.Currency;

@Data
public class InstrumentRequest {
    private String isin;
    private String ticker;
    private String name;
    private Currency currency;
}
