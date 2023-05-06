package com.github.it89.cfutils.marketdatastore.models;

import lombok.Data;

@Data
public class BondAmount {
    private AmountInfo nominalAmount;
    private AmountInfo aciAmount;
}
