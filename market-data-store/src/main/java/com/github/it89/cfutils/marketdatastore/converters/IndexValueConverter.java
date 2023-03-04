package com.github.it89.cfutils.marketdatastore.converters;

import com.github.it89.cfutils.marketdatastore.entities.IndexValueEntity;
import com.github.it89.cfutils.marketdatastore.models.IndexValue;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IndexValueConverter {
    public static IndexValue entityToDto(IndexValueEntity entity) {
        return IndexValue.builder()
                .instant(entity.getInstant())
                .value(entity.getValue())
                .build();
    }
}
