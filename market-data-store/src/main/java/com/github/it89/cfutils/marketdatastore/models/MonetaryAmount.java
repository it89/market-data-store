package com.github.it89.cfutils.marketdatastore.models;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
public class MonetaryAmount {
    BigDecimal amount;
    Currency currency;
}
