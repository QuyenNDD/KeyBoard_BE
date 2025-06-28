package com.project.keyboard.system;

import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getListProduct();
    Product createProduct(ProductRequestDTO productRequestDTO);
    boolean disableProduct(int productId);
    void updateProductWithImages(ProductUpdateDTO dto, MultipartFile[] images);
    void updateProduct(ProductUpdateDTO dto);
}
