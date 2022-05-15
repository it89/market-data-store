package com.github.it89.cfutils.tcs.client.tcs;

import com.github.it89.cfutils.tcs.client.store.dto.LastPrice;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class LastPriceConverter {
    public static LastPrice toDto(ru.tinkoff.piapi.contract.v1.LastPrice lastPrice) {
        var timestamp = lastPrice.getTime();
        var instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        return new LastPrice(ConvertUtils.quotationToBigDecimal(lastPrice.getPrice()), instant);
    }
}
