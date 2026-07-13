package com.fitting.cartservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Carrito de compras retornado por la API")
public class CartResponse {

    @Schema(description = "ID del carrito", example = "1")
    private Long id;

    @Schema(description = "ID del usuario dueño del carrito", example = "1")
    private Long userId;

    @Schema(description = "Total del carrito", example = "59.97")
    private BigDecimal totalAmount;

    @Schema(description = "Cantidad de items en el carrito", example = "3")
    private int itemCount;

    @Schema(description = "Items del carrito")
    private List<CartItemResponse> items;

    @Schema(description = "Fecha de creación", example = "2026-05-22T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de actualización", example = "2026-05-22T10:45:00")
    private LocalDateTime updatedAt;
}