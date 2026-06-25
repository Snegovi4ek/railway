package com.example.railway.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * REST-сервис, отдающий начальные данные для React-приложения.
 * JSON отдаётся как файл — без Redis ObjectMapper, чтобы формат был обычным массивом.
 */
@Slf4j
@RestController
@RequestMapping("/api/trips")
public class TripApiController {

    public TripApiController() {
        log.info("TripApiController инициализирован");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTrips() throws IOException {
        log.debug("Запрос списка рейсов для frontend (JSON)");

        ClassPathResource resource = new ClassPathResource("data/trips.json");
        String body = resource.getContentAsString(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}
