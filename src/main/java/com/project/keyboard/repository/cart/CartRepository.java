package com.project.keyboard.repository.cart;

import com.project.keyboard.dto.response.cart.TotalCartDTO;

import java.util.List;

public interface CartRepository {
    List<TotalCartDTO> getTotalCart();
}
