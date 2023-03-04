package com.github.it89.cfutils.csv.uploader.store;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.List;

@FeignClient(value = "dataMarketCandles", url = "${api-gateway-url}/marketDataStore")
public interface CandlesFeignClient {
    @PostMapping("/instruments/{id:\\d+}/candles/upload")
    void uploadInstrumentDayCandles(@PathVariable("id") Long instrumentId,
                                    @RequestBody List<Candle> candles,
                                    @RequestParam Duration duration,
                                    @RequestHeader String source);
}
