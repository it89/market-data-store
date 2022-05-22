package com.github.it89.cfutils.tcs.client.tcs;

import lombok.experimental.UtilityClass;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;

@UtilityClass
public class ConvertUtils {
    public static BigDecimal quotationToBigDecimal(Quotation quotation) {
        if (quotation.getUnits() == 0 && quotation.getNano() == 0) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(quotation.getUnits()).add(BigDecimal.valueOf(quotation.getNano(), 9));
        }
    }

    public static BigDecimal moneyValueToBigDecimal(MoneyValue moneyValue) {
        if (moneyValue.getUnits() == 0 && moneyValue.getNano() == 0) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(moneyValue.getUnits()).add(BigDecimal.valueOf(moneyValue.getNano(), 9));
        }
    }
}
