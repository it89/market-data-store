package com.github.it89.cfutils.tcs.client.store.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
public class LastPrice {
    BigDecimal price;
    Instant time;
}
