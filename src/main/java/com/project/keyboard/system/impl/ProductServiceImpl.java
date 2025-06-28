package com.project.keyboard.system.impl;

import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.entity.Product;
import com.project.keyboard.entity.ProductCategory;
import com.project.keyboard.entity.ProductVariant;
import com.project.keyboard.repository.category.ProductCategoryRepository;
import com.project.keyboard.repository.product.ProductRepository;
import com.project.keyboard.repository.productVariant.ProductVariantRepository;
import com.project.keyboard.repository.user.UserRepository;
import com.project.keyboard.system.ProductService;
import com.project.keyboard.system.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<Product> getListProduct(){
        return productRepository.getListProduct();
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO){
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setBrand(productRequestDTO.getBrand());
        product.setDescription(productRequestDTO.getDescription());
        product.setStatus(productRequestDTO.isStatus());

        ProductCategory category = productCategoryRepository.findByName(productRequestDTO.getCategory());

        product.setCategory(category);

        List<ProductVariant> variants = productRequestDTO.getVariants().stream().map(variant -> {
            ProductVariant productVariant = new ProductVariant();
            productVariant.setColor(variant.getColor());
            productVariant.setPrice(variant.getPrice());
            productVariant.setStockQuantity(variant.getStockQuantity());
            productVariant.setImg(variant.getImg());
            productVariant.setSku(variant.getSku());
            productVariant.setProduct(product);
            return productVariant;
        }).toList();

        product.setVariants(variants);
        productRepository.save(product);
        return product;
    }

    @Override
    public boolean disableProduct(int productId){
        Product product = productRepository.findById(productId);
        if (product == null){
            throw new RuntimeException("Product not found" + productId);
        }
        return productRepository.softDeleteProductById(productId);
    }

    @Override
    public void updateProduct(ProductUpdateDTO dto){
        // Bước 1: Lấy sản phẩm từ DB
        Product existingProduct = productRepository.findById(dto.getProductId());
        if (existingProduct == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        // Bước 2: Cập nhật thông tin cơ bản của sản phẩm
        existingProduct.setName(dto.getName());
        existingProduct.setBrand(dto.getBrand());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setStatus(dto.isStatus());

        // Cập nhật danh mục
        ProductCategory category = productCategoryRepository.findByName(dto.getCategory());
        existingProduct.setCategory(category);

        // Bước 3: Xử lý biến thể
        for (ProductVariantUpdateDTO variantDTO : dto.getVariants()) {
            if (variantDTO.isDeleted()) {
                if (variantDTO.getVariantId() != 0) {
                    productVariantRepository.deleteById(variantDTO.getVariantId());
                }
            } else if (variantDTO.getVariantId() == 0) {
                // Biến thể mới
                ProductVariant newVariant = new ProductVariant();
                newVariant.setColor(variantDTO.getColor());
                newVariant.setPrice(variantDTO.getPrice());
                newVariant.setStockQuantity(variantDTO.getStockQuantity());
                newVariant.setImg(variantDTO.getImg());
                newVariant.setSku(variantDTO.getSku());
                newVariant.setProduct(existingProduct);
                productVariantRepository.save(newVariant);
            } else {
                // Cập nhật biến thể cũ
                ProductVariant variant = new ProductVariant();
                variant.setVariantId(variantDTO.getVariantId());
                variant.setColor(variantDTO.getColor());
                variant.setPrice(variantDTO.getPrice());
                variant.setStockQuantity(variantDTO.getStockQuantity());
                variant.setImg(variantDTO.getImg());
                variant.setSku(variantDTO.getSku());
                variant.setProduct(existingProduct);
                productVariantRepository.update(variant);
            }
        }

        // Cập nhật lại bảng product
        productRepository.update(existingProduct);
    }

    @Override
    public void updateProductWithImages(ProductUpdateDTO dto, MultipartFile[] images){
        Map<Integer, String> variantImageUrls = new HashMap<>();

        int imgIndex = 0;
        for (ProductVariantUpdateDTO v : dto.getVariants()) {
            if (v.isDeleted()) continue;

            if (v.isReplaceImage() && images != null && imgIndex < images.length) {
                // Tìm ảnh cũ
                if (v.getVariantId() != 0) {
                    ProductVariant old = productVariantRepository.findById(v.getVariantId());
                    if (old != null && old.getImg() != null) {
                        try {
                            String publicId = cloudinaryService.extractPublicId(old.getImg());
                            cloudinaryService.delete(publicId);
                        }catch (IOException e){
                            throw new RuntimeException("Lỗi khi xóa ảnh Cloudinary", e);
                        }

                    }
                }

                // Upload ảnh mới
                try {
                    String newUrl = cloudinaryService.upload(images[imgIndex++]);
                    variantImageUrls.put(v.getVariantId(), newUrl);
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi khi upload ảnh Cloudinary", e);
                }
            }
        }

        // Gán ảnh mới vào DTO
        for (ProductVariantUpdateDTO v : dto.getVariants()) {
            if (variantImageUrls.containsKey(v.getVariantId())) {
                v.setImg(variantImageUrls.get(v.getVariantId()));
            }
        }

        // Gọi xử lý cập nhật logic trong Service (viết rồi ở updateProduct)
        updateProduct(dto);
    }


}
