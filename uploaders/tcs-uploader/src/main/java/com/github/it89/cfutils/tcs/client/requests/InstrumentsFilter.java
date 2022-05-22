package com.github.it89.cfutils.tcs.client.requests;

import lombok.Data;

import java.util.Set;

@Data
public class InstrumentsFilter {
    private Set<String> figiSet;
    private Set<String> isinSet;
}
