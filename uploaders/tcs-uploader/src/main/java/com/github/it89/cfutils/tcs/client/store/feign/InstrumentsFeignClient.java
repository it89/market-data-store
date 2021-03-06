package com.github.it89.cfutils.tcs.client.store.feign;

import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "dataMarketInstruments", url = "localhost:8100/instruments")
public interface InstrumentsFeignClient {
    @PostMapping("/upload")
    List<Instrument> upload(@RequestBody List<Instrument> instruments);
}
