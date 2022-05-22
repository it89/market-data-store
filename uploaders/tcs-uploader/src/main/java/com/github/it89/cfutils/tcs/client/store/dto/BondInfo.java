package com.github.it89.cfutils.tcs.client.store.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Value
@Builder
public class BondInfo {
    BigDecimal nominalValue;
    Currency nominalCurrency;
    BigDecimal aciValue;
    Currency aciCurrency;
    Instant time;
}
