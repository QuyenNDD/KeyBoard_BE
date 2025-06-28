package com.project.keyboard.system.impl;

import com.project.keyboard.dto.response.cart.TotalCartDTO;
import com.project.keyboard.repository.cart.CartRepository;
import com.project.keyboard.system.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<TotalCartDTO> getTotalCart(){
        return cartRepository.getTotalCart();
    }
}
