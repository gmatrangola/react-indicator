package com.matrangola.global.controller;

import com.matrangola.global.data.model.Indicator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/global")
public class GlobalController {

    @GetMapping("/{code}")
    public Mono<Double> totalIndicator(@PathVariable String code) {
        return all(code)
                .filter(indicator -> indicator.getYear2017() != null)
                .map(Indicator::getYear2017)
                .reduce((c1, c2) -> c1 + c2);
    }

    @GetMapping("/all/{code}")
    public Flux<Indicator> all(@PathVariable String code) {
        WebClient client = WebClient
                .builder()
                .baseUrl("http://localhost:8080/indicator/")
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return client.get()
                .uri(ubuild ->
                        ubuild.path("index").queryParam("indexCode", code).build())
                .retrieve()
                .bodyToFlux(Indicator.class);
    }
}