package com.fitting.cartservice.service;

import com.fitting.cartservice.dto.CartItemRequest;
import com.fitting.cartservice.dto.CartItemUpdateRequest;
import com.fitting.cartservice.dto.CartResponse;

public interface CartService {

    CartResponse getOrCreateCart(Long userId);

    CartResponse addItem(Long userId, CartItemRequest request);

    CartResponse updateItemQuantity(Long userId, Long productId, CartItemUpdateRequest request);

    CartResponse removeItem(Long userId, Long productId);

    CartResponse clearCart(Long userId);

    void deleteCart(Long userId);
}