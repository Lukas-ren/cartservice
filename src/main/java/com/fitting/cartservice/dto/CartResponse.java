package com.fitting.cartservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private int itemCount;
    private List<CartItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}