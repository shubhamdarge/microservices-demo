package com.example.payment.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final WebClient orderWebClient;

    @PostMapping
    public Mono<ResponseEntity<Map<String,Object>>> pay(@RequestParam Long orderId) {
        return orderWebClient.get()
                .uri("/api/orders/{id}", orderId)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(order -> {
                    BigDecimal amount = new BigDecimal(order.get("totalAmount").toString());
                    String newStatus = amount.compareTo(new BigDecimal("100")) < 0 ? "PAID" : "FAILED";

                    return orderWebClient.post()
                            .uri("/api/orders/{id}/status?status={s}", orderId, newStatus)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .map(updated -> ResponseEntity.ok(
                                    Map.of("orderId", orderId, "result", newStatus)));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}