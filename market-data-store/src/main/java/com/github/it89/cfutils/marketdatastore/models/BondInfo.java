package com.github.it89.cfutils.marketdatastore.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
public class BondInfo {
    private BigDecimal nominalValue;
    private Currency nominalCurrency;
    private BigDecimal aciValue;
    private Currency aciCurrency;
    private Instant time;
}
