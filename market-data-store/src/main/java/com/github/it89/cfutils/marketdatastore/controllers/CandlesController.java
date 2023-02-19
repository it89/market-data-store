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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.SortedMap;

@RestController
@RequiredArgsConstructor
public class CandlesController {
    private final CandlesService candlesService;
    private final CandleValuesService candleValuesService;

    @PostMapping("/candles/{figi}/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadDayCandles(@PathVariable String figi,
                                 @RequestBody List<Candle> candles,
                                 @RequestParam Duration duration,
                                 @RequestHeader(required = false) String source) {
        candlesService.upload(figi, candles, duration, source);
    }

    @PostMapping("/instruments/{id:\\d+}/candles/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void uploadInstrumentDayCandles(@PathVariable("id") Long instrumentId,
                                    @RequestBody List<Candle> candles,
                                    @RequestParam Duration duration,
                                    @RequestHeader(required = false) String source) {
        candlesService.upload(instrumentId, candles, duration, source);
    }

    @GetMapping("/instruments/{id:\\d+}/candles/values")
    public SortedMap<Instant, BigDecimal> getValues(@PathVariable("id") Long instrumentId) {
        return candleValuesService.getValues(instrumentId);
    }

    @GetMapping("/candles/{figi}/values")
    public SortedMap<Instant, BigDecimal> getValuesByFigi(@PathVariable String figi) {
        return candleValuesService.getValues(figi);
    }
}
