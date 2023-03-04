package com.github.it89.cfutils.csv.uploader.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexValue {
    private Instant instant;
    private BigDecimal value;
}
