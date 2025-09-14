package com.example.order.domain;

import javax.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String status; // CREATED, PAID, FAILED

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;
}