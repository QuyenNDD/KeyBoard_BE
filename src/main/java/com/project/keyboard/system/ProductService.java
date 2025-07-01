package com.project.keyboard.system;

import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getListProduct();
    Product createProduct(ProductRequestDTO productRequestDTO);
    boolean disableProduct(int productId);
    void updateProductWithImages(ProductUpdateDTO dto, List<MultipartFile> productImages,
                                 Map<Integer, MultipartFile> variantImageFiles);
    void updateProduct(ProductUpdateDTO dto);
}
