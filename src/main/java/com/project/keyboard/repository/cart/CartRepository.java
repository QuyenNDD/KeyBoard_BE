package com.project.keyboard.repository.cart;

import com.project.keyboard.dto.response.cart.TotalCartDTO;

import java.util.List;

public interface CartRepository {
    List<TotalCartDTO> getTotalCart();
    int addToCart(int userId, int variantId, int quantity);
    boolean isItemExist(int userId, int variantId);
    int updateQuantity(int userId, int variantId, int quantity);
}
