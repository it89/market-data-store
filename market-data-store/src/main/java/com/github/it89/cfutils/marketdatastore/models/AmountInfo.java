package com.github.it89.cfutils.marketdatastore.models;

import lombok.Value;

import java.time.Instant;

@Value
public class AmountInfo {
    MonetaryAmount amount;
    Instant time;
}
