package com.github.it89.cfutils.csv.uploader.parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.it89.cfutils.csv.uploader.ParseException;
import com.github.it89.cfutils.csv.uploader.store.Candle;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@UtilityClass
@Slf4j
public class CandlesCsvParser {
    public static List<Candle> parse(byte[] fileBytes, DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        mapper.findAndRegisterModules();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);

        try (MappingIterator<CandleCsvDto> mappingIterator = mapper
                .readerFor(CandleCsvDto.class)
                .with(bootstrapSchema)
                .readValues(fileBytes)) {

            List<CandleCsvDto> candlesCsv = mappingIterator.readAll();
            return candlesCsv.stream()
                    .map(it -> CandlesCsvParser.convert(it, dateTimeFormatter, zoneId))
                    .toList();
        } catch (IOException e) {
            log.error("Failed to parse CSV", e);
            throw new ParseException("Failed to parse CSV", e);
        }
    }

    private static Candle convert(CandleCsvDto csv, DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        return Candle.builder()
                .openTime(getOpenTime(csv.getDate(), dateTimeFormatter, zoneId))
                .open(csv.getOpen())
                .close(csv.getClose())
                .isComplete(true)
                .build();
    }

    private Instant getOpenTime(String value, DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        return LocalDate.parse(value, dateTimeFormatter).atStartOfDay().atZone(zoneId).toInstant();
    }
}
