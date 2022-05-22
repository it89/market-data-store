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

@Getter
@Setter
@Entity
@Table(name = "last_prices",
        uniqueConstraints = {@UniqueConstraint(name = "last_prices_ui", columnNames = {"time", "figi"})})
public class LastPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String figi;
    private BigDecimal price;
    private Instant time;
}
