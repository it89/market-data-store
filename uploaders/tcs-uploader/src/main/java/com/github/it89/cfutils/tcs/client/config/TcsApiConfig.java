package com.github.it89.cfutils.tcs.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.MarketDataService;

@Configuration
public class TcsApiConfig {
    @Bean
    public InvestApi investApi(@Value("${tcs.api.token}") String token) {
        return InvestApi.createReadonly(token);
    }

    @Bean
    public InstrumentsService investApiInstrumentsService(InvestApi investApi) {
        return investApi.getInstrumentsService();
    }

    @Bean
    public MarketDataService marketDataService(InvestApi investApi) {
        return investApi.getMarketDataService();
    }
}
