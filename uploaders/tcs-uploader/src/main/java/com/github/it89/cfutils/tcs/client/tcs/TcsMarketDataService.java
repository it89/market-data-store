package com.github.it89.cfutils.tcs.client.tcs;

import com.github.it89.cfutils.tcs.client.store.dto.Candle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.MarketDataService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TcsMarketDataService {
    private final MarketDataService marketDataService;

    public List<Candle> getCandles(String figi, Instant from, Instant to, CandleInterval candleInterval) {
        log.info("Get candles from TCS (figi={})", figi);
        List<HistoricCandle> candles = marketDataService.getCandlesSync(figi, from, to, candleInterval);

        return candles.stream()
                .map(CandlesConverter::historicCandleToDayCandle)
                .collect(Collectors.toList());
    }
}
