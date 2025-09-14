package com.example.order.client;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private boolean active;
}