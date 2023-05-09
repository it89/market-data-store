package com.github.it89.cfutils.marketdatastore.models;

import lombok.Data;

@Data
public class InstrumentAmountInfo {
    private Instrument instrument;
    private PriceInfo priceInfo;
    private BondAmount bondAmount;
    private AmountInfo amountInfo;
}
