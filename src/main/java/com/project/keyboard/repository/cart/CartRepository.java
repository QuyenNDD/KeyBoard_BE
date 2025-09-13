package com.project.keyboard.repository.cart;

import com.project.keyboard.dto.response.cart.CartUserResponse;
import com.project.keyboard.dto.response.cart.TotalCartDTO;
import com.project.keyboard.entity.Cart;

import java.util.List;

public interface CartRepository {
    List<TotalCartDTO> getTotalCart();
    int addToCart(int userId, int variantId, int quantity);
    boolean isItemExist(int userId, int variantId);
    int updateQuantity(int userId, int variantId, int quantity);
    boolean isCartBelongToUser(int userId, int cartId);
    List<CartUserResponse> getListCartBelongUser(int userId);
    int countCartBelongUser(int userId);
    Cart findById(int cartId, int userId);
    int deleteById(int cartId, int userId);
}
