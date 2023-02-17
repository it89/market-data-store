package com.github.it89.cfutils.tcs.client.store.feign;

import com.github.it89.cfutils.tcs.client.store.dto.Candle;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.List;

@FeignClient(value = "dataMarketCandles", url = "${api-gateway-url}/marketDataStore/candles")
public interface CandlesFeignClient {
    @PostMapping("/{figi}/upload")
    void upload(@PathVariable String figi,
                @RequestBody List<Candle> candles,
                @RequestParam Duration duration,
                @RequestHeader String source);
}
