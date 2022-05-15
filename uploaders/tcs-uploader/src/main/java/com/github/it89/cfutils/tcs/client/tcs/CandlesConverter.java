package com.github.it89.cfutils.tcs.client.tcs;

import com.github.it89.cfutils.tcs.client.store.dto.Candle;
import lombok.experimental.UtilityClass;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.time.Instant;

@UtilityClass
public class CandlesConverter {
    public static Candle historicCandleToDayCandle(HistoricCandle historicCandle) {
        var timestamp = historicCandle.getTime();
        var instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        return Candle.builder()
                .openTime(instant)
                .open(ConvertUtils.quotationToBigDecimal(historicCandle.getOpen()))
                .close(ConvertUtils.quotationToBigDecimal(historicCandle.getClose()))
                .isComplete(historicCandle.getIsComplete())
                .build();
    }
}
