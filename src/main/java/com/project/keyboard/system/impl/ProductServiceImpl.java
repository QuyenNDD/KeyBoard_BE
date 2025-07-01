package com.project.keyboard.system.impl;

import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.dto.response.revenue.TopSellingProductDTO;
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
import java.util.*;
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
        product.setImgs(productRequestDTO.getImgs());

        ProductCategory category = productCategoryRepository.findByName(productRequestDTO.getCategory());

        product.setCategory(category);

        List<ProductVariant> variants = new ArrayList<>();
        if (productRequestDTO.getVariants() != null) {
            variants = productRequestDTO.getVariants().stream().map(variantDTO -> {
                ProductVariant variant = new ProductVariant();
                variant.setColor(variantDTO.getColor());
                variant.setPrice(variantDTO.getPrice());
                variant.setStockQuantity(variantDTO.getStockQuantity());
                variant.setSku(variantDTO.getSku());
                variant.setImg(variantDTO.getImg());
                variant.setProduct(product);
                return variant;
            }).toList();
        };

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
    public void updateProductWithImages(ProductUpdateDTO dto,
                                        List<MultipartFile> productImages,
                                        Map<Integer, MultipartFile> variantImageFiles) {

        // ===== 1️⃣ Xử lý ẢNH SẢN PHẨM CHUNG =====

        // Lấy sản phẩm gốc
        Product existingProduct = productRepository.findById(dto.getProductId());
        List<String> oldImgsInDb = new ArrayList<>();
        if (existingProduct.getImgs() != null && !existingProduct.getImgs().isEmpty()) {
            oldImgsInDb = Arrays.asList(existingProduct.getImgs().split(";"));
        }

        // FE gửi những ảnh muốn giữ lại
        List<String> keepImgs = dto.getProductImgDTO() != null ? dto.getProductImgDTO().getExistingImg() : new ArrayList<>();

        // Tìm ảnh cần xoá
        List<String> toDelete = oldImgsInDb.stream()
                .filter(old -> !keepImgs.contains(old))
                .toList();

        // Xoá trên Cloudinary
        for (String img : toDelete) {
            try {
                String publicId = cloudinaryService.extractPublicId(img);
                cloudinaryService.delete(publicId);
            }catch (IOException e) {
                throw new RuntimeException("Upload ảnh thất bại!", e);
            }

        }

        // Thêm ảnh mới
        List<String> allProductImgs = new ArrayList<>(keepImgs);
        if (productImages != null && !productImages.isEmpty()) {
            for (MultipartFile img : productImages) {
                try {
                    String url = cloudinaryService.upload(img);
                    allProductImgs.add(url);
                }catch (IOException e) {
                    throw new RuntimeException("Upload ảnh thất bại!", e);
                }

            }
        }
        dto.setImgs(String.join(";", allProductImgs));

        // ===== 2️⃣ Xử lý BIẾN THỂ =====

        for (int i = 0; i < dto.getVariants().size(); i++) {
            ProductVariantUpdateDTO v = dto.getVariants().get(i);

            if (v.isDeleted()) {
                if (v.getVariantId() != 0 && v.getVariantId() > 0) {
                    productVariantRepository.deleteVariantById(v.getVariantId());
                }
                continue;
            }

            String finalVariantImg = null;

            // Có file mới ➜ upload, ghi đè
            if (variantImageFiles.containsKey(i)) {
                try {
                    finalVariantImg = cloudinaryService.upload(variantImageFiles.get(i));
                }catch (IOException e) {
                    throw new RuntimeException("Upload ảnh thất bại!", e);
                }

            } else {
                // Giữ link cũ nếu có
                if (v.getVariantImgDTO() != null && v.getVariantImgDTO().getExistingImg() != null && !v.getVariantImgDTO().getExistingImg().isEmpty()) {
                    finalVariantImg = v.getVariantImgDTO().getExistingImg().get(0);
                }
            }

            v.setImg(finalVariantImg);

            if (v.getVariantId() != 0 && v.getVariantId() > 0) {
                productVariantRepository.updateVariant(v);
            } else {
                productVariantRepository.insertVariant(v, dto.getProductId());
            }
        }

        // ===== 3️⃣ Cập nhật bảng products =====

        productRepository.updateProduct(dto);
    }

    @Override
    public List<TopSellingProductDTO> getTopSellingProduct(int limit){
        return productRepository.getTopSellingProduct(limit);
    }

}
