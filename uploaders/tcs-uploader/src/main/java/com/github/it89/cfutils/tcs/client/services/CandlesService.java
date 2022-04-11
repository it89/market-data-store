package com.github.it89.cfutils.tcs.client.services;

import com.github.it89.cfutils.tcs.client.store.dto.Candle;
import com.github.it89.cfutils.tcs.client.store.feign.CandlesFeignClient;
import com.github.it89.cfutils.tcs.client.tcs.TcsMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandlesService {
    private final TcsMarketDataService tcsCandlesService;
    private final CandlesFeignClient candlesFeignClient;

    public void uploadCandles(String figi, LocalDate from, LocalDate to) {
        List<Candle> candles = tcsCandlesService.getCandles(
                figi,
                from.atStartOfDay().toInstant(ZoneOffset.UTC),
                to.atStartOfDay().toInstant(ZoneOffset.UTC),
                CandleInterval.CANDLE_INTERVAL_DAY);

        log.info("Save candles to market-data-store");
        candlesFeignClient.upload(figi, candles, Duration.ofDays(1));
    }
}
