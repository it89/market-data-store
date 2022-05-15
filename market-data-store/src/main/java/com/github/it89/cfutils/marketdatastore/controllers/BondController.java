package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.BondInfo;
import com.github.it89.cfutils.marketdatastore.services.BondAciService;
import com.github.it89.cfutils.marketdatastore.services.BondNominalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bonds")
@RequiredArgsConstructor
public class BondController {
    private final BondNominalService bondNominalService;
    private final BondAciService bondAciService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestBody Map<String, BondInfo> figiBondInfoMap) {
        bondNominalService.upload(figiBondInfoMap);
        bondAciService.upload(figiBondInfoMap);
    }
}
