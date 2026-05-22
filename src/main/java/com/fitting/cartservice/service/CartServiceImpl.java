package com.fitting.cartservice.service;

import com.fitting.cartservice.dto.CartItemRequest;
import com.fitting.cartservice.dto.CartItemUpdateRequest;
import com.fitting.cartservice.dto.CartItemResponse;
import com.fitting.cartservice.dto.CartResponse;
import com.fitting.cartservice.entity.Cart;
import com.fitting.cartservice.entity.CartItem;
import com.fitting.cartservice.exception.ResourceNotFoundException;
import com.fitting.cartservice.repository.CartItemRepository;
import com.fitting.cartservice.repository.CartRepository;
import com.fitting.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // ── Obtener o crear carrito ─────────────────────────────────────────────────

    @Override
    @Transactional
    public CartResponse getOrCreateCart(Long userId) {
        log.debug("Obteniendo carrito para usuario ID: {}", userId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));
        return toResponse(cart);
    }

    // ── Agregar item ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CartResponse addItem(Long userId, CartItemRequest request) {
        log.info("Agregando producto ID {} al carrito de usuario ID: {}",
                request.getProductId(), userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        // Si el producto ya existe en el carrito, sumar cantidad
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), request.getProductId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.recalculateSubtotal();
            cartItemRepository.save(item);
            log.debug("Cantidad actualizada para producto ID: {}", request.getProductId());
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(request.getProductId())
                    .productName(request.getProductName())
                    .unitPrice(request.getUnitPrice())
                    .quantity(request.getQuantity())
                    .cart(cart)
                    .build();
            newItem.recalculateSubtotal();
            cart.getItems().add(newItem);
            log.debug("Nuevo item agregado — producto ID: {}", request.getProductId());
        }

        cart.recalculateTotal();
        return toResponse(cartRepository.save(cart));
    }

    // ── Actualizar cantidad ─────────────────────────────────────────────────────

    @Override
    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long productId,
                                           CartItemUpdateRequest request) {
        log.info("Actualizando cantidad de producto ID {} en carrito de usuario ID: {}",
                productId, userId);

        Cart cart = getCartOrThrow(userId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto ID " + productId + " no encontrado en el carrito"));

        item.setQuantity(request.getQuantity());
        item.recalculateSubtotal();
        cartItemRepository.save(item);

        cart.recalculateTotal();
        return toResponse(cartRepository.save(cart));
    }

    // ── Eliminar item ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CartResponse removeItem(Long userId, Long productId) {
        log.info("Eliminando producto ID {} del carrito de usuario ID: {}",
                productId, userId);

        Cart cart = getCartOrThrow(userId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto ID " + productId + " no encontrado en el carrito"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        cart.recalculateTotal();
        return toResponse(cartRepository.save(cart));
    }

    // ── Vaciar carrito ──────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CartResponse clearCart(Long userId) {
        log.info("Vaciando carrito de usuario ID: {}", userId);

        Cart cart = getCartOrThrow(userId);
        cart.getItems().clear();
        cartItemRepository.deleteByCartId(cart.getId());
        cart.recalculateTotal();

        return toResponse(cartRepository.save(cart));
    }

    // ── Eliminar carrito completo ───────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteCart(Long userId) {
        log.info("Eliminando carrito de usuario ID: {}", userId);
        Cart cart = getCartOrThrow(userId);
        cartRepository.delete(cart);
    }

    // ── Helper — crear carrito nuevo ────────────────────────────────────────────

    private Cart createNewCart(Long userId) {
        log.info("Creando nuevo carrito para usuario ID: {}", userId);
        Cart cart = Cart.builder()
                .userId(userId)
                .build();
        return cartRepository.save(cart);
    }

    // ── Helper repository ───────────────────────────────────────────────────────

    private Cart getCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Carrito para usuario ID " + userId + " no encontrado"));
    }

    // ── Mapper interno ──────────────────────────────────────────────────────────

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> CartItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .totalAmount(cart.getTotalAmount())
                .itemCount(itemResponses.size())
                .items(itemResponses)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}