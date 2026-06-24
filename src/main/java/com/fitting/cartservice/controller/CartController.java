package com.fitting.cartservice.controller;

import com.fitting.cartservice.dto.CartItemRequest;
import com.fitting.cartservice.dto.CartItemUpdateRequest;
import com.fitting.cartservice.dto.CartResponse;
import com.fitting.cartservice.service.CartService;
import com.fitting.cartservice.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Carrito", description = "Gestión del carrito de compras por usuario")
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Obtener o crear carrito", description = "Si el usuario no tiene carrito, lo crea automáticamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Carrito obtenido")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long userId) {
        log.info("GET /api/v1/cart/user/{}", userId);
        return ResponseEntity.ok(ApiResponse.ok("Carrito obtenido",
                cartService.getOrCreateCart(userId)));
    }

    @Operation(summary = "Agregar item al carrito", description = "Si el producto ya existe en el carrito, suma la cantidad")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Item agregado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequest request) {
        log.info("POST /api/v1/cart/user/{}/items", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Item agregado al carrito",
                        cartService.addItem(userId, request)));
    }

    @Operation(summary = "Actualizar cantidad de un item")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cantidad actualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito")
    })
    @PatchMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody CartItemUpdateRequest request) {
        log.info("PATCH /api/v1/cart/user/{}/items/{}", userId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Cantidad actualizada",
                cartService.updateItemQuantity(userId, productId, request)));
    }

    @Operation(summary = "Eliminar item del carrito")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Item eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito")
    })
    @DeleteMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        log.info("DELETE /api/v1/cart/user/{}/items/{}", userId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Item eliminado del carrito",
                cartService.removeItem(userId, productId)));
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los items pero mantiene el carrito")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Carrito vaciado")
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(@PathVariable Long userId) {
        log.info("DELETE /api/v1/cart/user/{}/clear", userId);
        return ResponseEntity.ok(ApiResponse.ok("Carrito vaciado",
                cartService.clearCart(userId)));
    }

    @Operation(summary = "Eliminar carrito completo")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Carrito eliminado")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable Long userId) {
        log.info("DELETE /api/v1/cart/user/{}", userId);
        cartService.deleteCart(userId);
        return ResponseEntity.ok(ApiResponse.ok("Carrito eliminado", null));
    }
}