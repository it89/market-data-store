package com.github.it89.cfutils.tcs.client.store.feign;

import com.github.it89.cfutils.tcs.client.store.dto.BondInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "dataMarketBonds", url = "localhost:8100/bonds")
public interface BondsFeignClient {
    @PostMapping("/upload")
    void upload(@RequestBody Map<String, BondInfo> figiBondInfoMap);
}
