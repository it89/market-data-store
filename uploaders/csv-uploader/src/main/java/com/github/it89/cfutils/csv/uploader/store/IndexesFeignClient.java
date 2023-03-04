package com.github.it89.cfutils.csv.uploader.store;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(value = "dataMarketIndexes", url = "${api-gateway-url}/marketDataStore")
public interface IndexesFeignClient {
    @PostMapping("/indexes/{indexCode}/values/upload")
    void uploadValues(@PathVariable String indexCode,
                      @RequestBody List<IndexValue> values,
                      @RequestHeader String source);
}
