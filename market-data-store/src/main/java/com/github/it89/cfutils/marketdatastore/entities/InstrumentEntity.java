package com.github.it89.cfutils.marketdatastore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Currency;

@Getter
@Setter
@Entity
@Table(name = "instruments")
public class InstrumentEntity {
    @Id
    private String figi;

    private String isin;
    private String ticker;
    private String name;
    private Currency currency;
}
