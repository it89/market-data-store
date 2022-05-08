package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.Candle;
import com.github.it89.cfutils.marketdatastore.services.CandleValuesService;
import com.github.it89.cfutils.marketdatastore.services.CandlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.SortedMap;

@RestController
@RequestMapping("/candles")
@RequiredArgsConstructor
public class CandlesController {
    private final CandlesService candlesService;
    private final CandleValuesService candleValuesService;

    @PostMapping("/{figi}/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadDayCandles(@PathVariable String figi,
                                 @RequestBody List<Candle> candles,
                                 @RequestParam Duration duration) {
        candlesService.upload(figi, candles, duration);
    }

    @GetMapping("/{figi}/values")
    public SortedMap<Instant, BigDecimal> getValues(@PathVariable String figi) {
        return candleValuesService.getValues(figi);
    }
}
