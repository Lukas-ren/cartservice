package com.fitting.cartservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al producto en product-service
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 150)
    private String productName;         // desnormalizado

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;        // unitPrice * quantity

    // Muchos items pertenecen a un carrito
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Recalcula el subtotal automáticamente
    public void recalculateSubtotal() {
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
}