package com.project.keyboard.system.impl;

import com.project.keyboard.dto.response.cart.CartUserResponse;
import com.project.keyboard.dto.response.cart.TotalCartDTO;
import com.project.keyboard.entity.Cart;
import com.project.keyboard.entity.Users;
import com.project.keyboard.repository.cart.CartRepository;
import com.project.keyboard.repository.user.UserRepository;
import com.project.keyboard.system.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TotalCartDTO> getTotalCart(){
        return cartRepository.getTotalCart();
    }

    @Override
    public String addToCart(int userId, int variantId, int quantity){
        Users user = userRepository.findById(userId);
        if (!user.isStatus()){
            return "Tài khoản đã bị khóa, không thể thêm sản phẩm";
        }
        if (cartRepository.isItemExist(userId, variantId)){
            cartRepository.updateQuantity(userId, variantId, quantity);
            return "Item updated in cart";
        }else {
            cartRepository.addToCart(userId, variantId, quantity);
            return "Item added to cart";
        }
    }

    @Override
    public List<CartUserResponse> getListCartBelongUser(int userId){
        return cartRepository.getListCartBelongUser(userId);
    }

    @Override
    public int countCartBelongUser(int userId){
        return cartRepository.countCartBelongUser(userId);
    }

    @Override
    public void deleteCart(int userId, int cartId){
//        Cart cart = cartRepository.findById(cartId, userId);
//        if (cart == null) {
//            throw new RuntimeException("Cart not found");
//        }
        int rs = cartRepository.deleteById(cartId, userId);
        if (rs == 0) {
            throw new RuntimeException("Cart not found");
        }
    }
}
