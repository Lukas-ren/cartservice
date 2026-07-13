package com.fitting.cartservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item del carrito retornado por la API")
public class CartItemResponse {

    @Schema(description = "ID del item", example = "1")
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productId;

    @Schema(description = "Nombre del producto", example = "Camiseta Básica Blanca")
    private String productName;

    @Schema(description = "Precio unitario", example = "19.99")
    private BigDecimal unitPrice;

    @Schema(description = "Cantidad", example = "2")
    private Integer quantity;

    @Schema(description = "Subtotal", example = "39.98")
    private BigDecimal subtotal;
}