package com.project.keyboard.system;

import com.project.keyboard.dto.response.cart.TotalCartDTO;

import java.util.List;

public interface CartService {
    List<TotalCartDTO> getTotalCart();
    String addToCart(int userId, int variantId, int quantity);
}
