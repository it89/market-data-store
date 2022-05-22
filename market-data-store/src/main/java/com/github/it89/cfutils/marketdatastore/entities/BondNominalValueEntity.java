package com.github.it89.cfutils.marketdatastore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Getter
@Setter
@Entity
@Table(name = "bond_nominal_values",
        uniqueConstraints = {@UniqueConstraint(name = "bond_nominal_values_ui", columnNames = {"time", "figi"})})
public class BondNominalValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String figi;
    private BigDecimal nominalValue;
    private Currency currency;
    private Instant time;
}
