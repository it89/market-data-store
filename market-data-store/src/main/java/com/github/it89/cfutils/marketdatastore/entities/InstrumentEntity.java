package com.github.it89.cfutils.marketdatastore.entities;

import com.github.it89.cfutils.marketdatastore.models.InstrumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
