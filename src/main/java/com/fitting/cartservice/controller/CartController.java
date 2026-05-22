package com.fitting.cartservice.controller;

import com.fitting.cartservice.dto.CartItemRequest;
import com.fitting.cartservice.dto.CartItemUpdateRequest;
import com.fitting.cartservice.dto.CartResponse;
import com.fitting.cartservice.service.CartService;
import com.fitting.cartservice.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Obtener o crear carrito del usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long userId) {
        log.info("GET /api/v1/cart/user/{}", userId);
        return ResponseEntity.ok(ApiResponse.ok("Carrito obtenido",
                cartService.getOrCreateCart(userId)));
    }

    // Agregar item al carrito
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequest request) {
        log.info("POST /api/v1/cart/user/{}/items", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Item agregado al carrito",
                        cartService.addItem(userId, request)));
    }

    // Actualizar cantidad de un item
    @PatchMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody CartItemUpdateRequest request) {
        log.info("PATCH /api/v1/cart/user/{}/items/{}", userId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Cantidad actualizada",
                cartService.updateItemQuantity(userId, productId, request)));
    }

    // Eliminar un item del carrito
    @DeleteMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        log.info("DELETE /api/v1/cart/user/{}/items/{}", userId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Item eliminado del carrito",
                cartService.removeItem(userId, productId)));
    }

    // Vaciar carrito (mantiene el carrito, elimina los items)
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(@PathVariable Long userId) {
        log.info("DELETE /api/v1/cart/user/{}/clear", userId);
        return ResponseEntity.ok(ApiResponse.ok("Carrito vaciado",
                cartService.clearCart(userId)));
    }

    // Eliminar carrito completo
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable Long userId) {
        log.info("DELETE /api/v1/cart/user/{}", userId);
        cartService.deleteCart(userId);
        return ResponseEntity.ok(ApiResponse.ok("Carrito eliminado", null));
    }
}