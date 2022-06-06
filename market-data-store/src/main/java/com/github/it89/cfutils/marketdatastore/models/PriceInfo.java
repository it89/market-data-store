package com.github.it89.cfutils.marketdatastore.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PriceInfo {
    private BigDecimal price;
    private Instant time;
}
