package com.github.it89.cfutils.csv.uploader.parser;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CandleCsvDto {
    private String date;
    private BigDecimal open;
    private BigDecimal close;
}
