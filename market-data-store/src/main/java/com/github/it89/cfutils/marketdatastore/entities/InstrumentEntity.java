package com.github.it89.cfutils.marketdatastore.entities;

import com.github.it89.cfutils.marketdatastore.models.InstrumentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Currency;

@Getter
@Setter
@Entity
@Table(name = "instruments")

public class InstrumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String figi;

    private String isin;
    private String ticker;
    private String name;
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstrumentType type;
}
