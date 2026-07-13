package com.fitting.cartservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para agregar un item al carrito")
public class CartItemRequest {

    @Schema(description = "ID del producto", example = "1")
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;

    @Schema(description = "Nombre del producto", example = "Camiseta Básica Blanca")
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150)
    private String productName;

    @Schema(description = "Precio unitario del producto", example = "19.99")
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    @Schema(description = "Cantidad a agregar", example = "2")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1)
    private Integer quantity;
}