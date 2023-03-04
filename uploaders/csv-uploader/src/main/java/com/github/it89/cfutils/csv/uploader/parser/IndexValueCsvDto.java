package com.github.it89.cfutils.csv.uploader.parser;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndexValueCsvDto {
    private String date;
    private BigDecimal value;
}
