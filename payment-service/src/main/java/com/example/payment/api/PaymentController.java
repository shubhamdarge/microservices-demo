package com.example.payment.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final WebClient orderWebClient;

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> pay(@RequestParam Long orderId) {
        return orderWebClient.get()
                .uri("/api/orders/{id}", orderId)
                .retrieve()
                .bodyToMono(Map.class) // order from order-service
                .flatMap(order -> {
                    // Validate payload
                    if (order == null || order.get("totalAmount") == null) {
                        Map<String, Object> err = new HashMap<>();
                        err.put("error", "invalid order payload");
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err));
                    }

                    // Decide payment result
                    BigDecimal amount = new BigDecimal(order.get("totalAmount").toString());
                    String newStatus = amount.compareTo(new BigDecimal("100")) < 0 ? "PAID" : "FAILED";

                    // Update order status back in order-service
                    return orderWebClient.post()
                            .uri("/api/orders/{id}/status?status={s}", orderId, newStatus)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .map(updated -> {
                                Map<String, Object> payload = new HashMap<>();
                                payload.put("orderId", orderId);
                                payload.put("result", newStatus);
                                return ResponseEntity.ok(payload);
                            });
                })
                // If order-service returned 404 (empty Mono), return a typed 404 body (Map) — not ResponseEntity<Void>
                .defaultIfEmpty(
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Collections.<String, Object>singletonMap("error", "order not found"))
                );
    }
}