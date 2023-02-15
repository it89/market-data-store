package com.github.it89.cfutils.marketdatastore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "candles",
        uniqueConstraints = {@UniqueConstraint(
                name = "candles_ui",
                columnNames = {"openTime", "instrument_id", "duration"})})
public class CandleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private InstrumentEntity instrument;

    private Instant openTime;
    private BigDecimal open;
    private BigDecimal close;
    private boolean isComplete;
    private Duration duration;
}
