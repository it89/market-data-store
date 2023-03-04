package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.IndexInfo;
import com.github.it89.cfutils.marketdatastore.models.IndexValue;
import com.github.it89.cfutils.marketdatastore.services.IndexesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IndexesController {
    private final IndexesService indexesService;

    @PostMapping("/indexes")
    public IndexInfo createIndex(@RequestBody IndexInfo indexInfo) {
        log.info("Create index info {}", indexInfo);
        return indexesService.createIndex(indexInfo);
    }

    @GetMapping("/indexes/{indexCode}/values")
    public List<IndexValue> getValues(@PathVariable String indexCode) {
        return indexesService.getValues(indexCode);
    }

    @PostMapping("/indexes/{indexCode}/values/upload")
    public void uploadValues(@PathVariable String indexCode,
                             @RequestBody List<IndexValue> values,
                             @RequestHeader(required = false) String source) {
        log.info("Upload values for index {}", indexCode);
        indexesService.uploadValues(indexCode, values, source);
    }
}
