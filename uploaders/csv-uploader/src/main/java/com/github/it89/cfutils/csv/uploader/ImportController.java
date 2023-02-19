package com.github.it89.cfutils.csv.uploader;

import com.github.it89.cfutils.csv.uploader.parser.CandlesCsvParser;
import com.github.it89.cfutils.csv.uploader.store.Candle;
import com.github.it89.cfutils.csv.uploader.store.CandlesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImportController {
    private final CandlesFeignClient candlesFeignClient;

    @PostMapping("/instruments/{id:\\d+}/candles/import")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void importCsv(@PathVariable("id") Long instrumentId,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("dateFormat") String  dateFormat,
                          @RequestParam("zoneId") ZoneId zoneId,
                          @RequestHeader(required = false) String source
    ) {
        log.info("Upload candles for instrumentId={}", instrumentId);
        byte[] fileBytes;
        try (InputStream inputStream = file.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Failed to parse file", e);
            throw new ParseException("Failed to parse file", e);
        }

        List<Candle> candles = CandlesCsvParser.parse(fileBytes, DateTimeFormatter.ofPattern(dateFormat), zoneId);
        candlesFeignClient.uploadInstrumentDayCandles(instrumentId, candles, Duration.ofDays(1), source);
    }
}
