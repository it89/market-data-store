package com.github.it89.cfutils.marketdatastore.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class Candle {
    private Instant openTime;
    private BigDecimal open;
    private BigDecimal close;
    private boolean isComplete;
}
