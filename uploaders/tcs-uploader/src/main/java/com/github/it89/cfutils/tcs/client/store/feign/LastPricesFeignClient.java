package com.github.it89.cfutils.tcs.client.store.feign;

import com.github.it89.cfutils.tcs.client.store.dto.LastPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "dataMarketLastPrices", url = "localhost:8100/lastPrices")
public interface LastPricesFeignClient {
    @PostMapping("/upload")
    void upload(@RequestBody Map<String, LastPrice> figiLastPriceMap);
}
