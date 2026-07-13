package com.fitting.cartservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para actualizar la cantidad de un item del carrito")
public class CartItemUpdateRequest {

    @Schema(description = "Nueva cantidad del item", example = "3")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1)
    private Integer quantity;
}