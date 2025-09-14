package com.example.order.api;

import com.example.order.domain.Order;
import com.example.order.repo.OrderRepository;
import com.example.order.client.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repo;
    private final WebClient userWebClient;

    @PostMapping
    public Mono<ResponseEntity<Order>> create(@RequestParam Long userId,
                                              @RequestParam @Min(0) BigDecimal amount) {
        return userWebClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .map(u -> {
                    Order o = Order.builder()
                            .userId(u.getId())
                            .status("CREATED")
                            .totalAmount(amount)
                            .build();
                    Order saved = repo.save(o);
                    return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
                })
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status){
        return repo.findById(id).map(o -> {
            o.setStatus(status);
            return ResponseEntity.ok(repo.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }
}