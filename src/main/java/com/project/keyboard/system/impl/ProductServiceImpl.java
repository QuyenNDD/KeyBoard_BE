package com.project.keyboard.system.impl;

import com.project.keyboard.dto.request.*;
import com.project.keyboard.dto.response.FilterProductResponse;
import com.project.keyboard.dto.response.category.DetailCatgoryDTO;
import com.project.keyboard.dto.response.product.NewProductDTO;
import com.project.keyboard.dto.response.product.ProductResponeDTO;
import com.project.keyboard.dto.response.product.ProductVariantResponeDTO;
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
    public List<ProductResponeDTO> getListProduct(int page, int size){
        List<Product> products = productRepository.getListProduct(page, size);

        List<ProductResponeDTO> result = new ArrayList<>();

        for (Product product : products) {
            ProductResponeDTO dto = new ProductResponeDTO();
            dto.setProductId(product.getProductId());
            dto.setName(product.getName());
            dto.setBrand(product.getBrand());
            dto.setMinPrice(product.getMinPrice());
            dto.setDescription(product.getDescription());
            dto.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);

            // Ảnh sản phẩm
            ProductImgDTO productImgDTO = new ProductImgDTO();
            if (product.getImgs() != null && !product.getImgs().isEmpty()) {
                productImgDTO.setExistingImg(Arrays.asList(product.getImgs().split(";")));
            } else {
                productImgDTO.setExistingImg(new ArrayList<>());
            }
            dto.setProductImg(productImgDTO);

            // Biến thể
            List<ProductVariantResponeDTO> variants = productVariantRepository.findByProductId(product.getProductId())
                    .stream()
                    .map(v -> {
                        ProductVariantResponeDTO vv = new ProductVariantResponeDTO();
                        vv.setVariantId(v.getVariantId());
                        vv.setColor(v.getColor());
                        vv.setPrice(v.getPrice().doubleValue());
                        vv.setStockQuantity(v.getStockQuantity());
                        vv.setSku(v.getSku());
                        vv.setDeleted(false);

                        VariantImgDTO variantImgDTO = new VariantImgDTO();
                        if (v.getImg() != null && !v.getImg().isEmpty()) {
                            variantImgDTO.setExistingImg(Arrays.asList(v.getImg().split(";")));
                        } else {
                            variantImgDTO.setExistingImg(new ArrayList<>());
                        }
                        vv.setVariantImg(variantImgDTO);

                        return vv;
                    }).collect(Collectors.toList());
            dto.setVariants(variants);

            result.add(dto);
        }
        return result;
    }

    @Override
    public int countProducts(){
        return productRepository.countProducts();
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO){
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setBrand(productRequestDTO.getBrand());
        product.setDescription(productRequestDTO.getDescription());
        product.setStatus(productRequestDTO.isStatus());
        product.setImgs(productRequestDTO.getImgs());

        ProductCategory category = productCategoryRepository.findById(productRequestDTO.getCategoryId());

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
        Product existingProduct = productRepository.findProductById(dto.getProductId());
        if (existingProduct == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        // Bước 2: Cập nhật thông tin cơ bản của sản phẩm
        existingProduct.setName(dto.getName());
        existingProduct.setBrand(dto.getBrand());
        existingProduct.setDescription(dto.getDescription());
        // Cập nhật danh mục
        ProductCategory category = productCategoryRepository.findById(dto.getCategoryId());
        existingProduct.setCategory(category);

        // Bước 3: Xử lý biến thể
        for (ProductVariantUpdateDTO variantDTO : dto.getVariants()) {
            String rawId = variantDTO.getVariantId();
            boolean isExisting = rawId != null && rawId.matches("\\d+"); // true nếu id là số

            // 1️⃣ Xóa biến thể
            if (variantDTO.isDeleted()) {
                if (isExisting) {
                    int variantId = Integer.parseInt(rawId);
                    productVariantRepository.deleteById(variantId);
                }
                continue;
            }

            // 2️⃣ Thêm biến thể mới (id = null hoặc id tạm "a","b","c")
            if (!isExisting) {
                ProductVariant newVariant = new ProductVariant();
                newVariant.setColor(variantDTO.getColor());
                newVariant.setPrice(variantDTO.getPrice());
                newVariant.setStockQuantity(variantDTO.getStockQuantity());
                newVariant.setImg(variantDTO.getImg());
                newVariant.setSku(variantDTO.getSku());
                newVariant.setProduct(existingProduct);

                productVariantRepository.save(newVariant);
                continue;
            }

            // 3️⃣ Cập nhật biến thể cũ (id là số)
            int variantId = Integer.parseInt(rawId);
            ProductVariant variant = productVariantRepository.findById(variantId);
            if (variant == null) {
                throw new RuntimeException("Không tìm thấy biến thể id=" + variantId);
            }

            variant.setColor(variantDTO.getColor());
            variant.setPrice(variantDTO.getPrice());
            variant.setStockQuantity(variantDTO.getStockQuantity());
            variant.setImg(variantDTO.getImg());
            variant.setSku(variantDTO.getSku());
            variant.setProduct(existingProduct);

            productVariantRepository.update(variant);
        }

        // Cập nhật lại bảng product
        productRepository.update(existingProduct);
    }

    @Override
    public void updateProductWithImages(ProductUpdateDTO dto,
                                        List<MultipartFile> productImages,
                                        Map<String, List<MultipartFile>> variantImageFiles) {

        // ===== 1️⃣ Xử lý ẢNH SẢN PHẨM CHUNG =====
        // Lấy sản phẩm gốc
        Product existingProduct = productRepository.findById(dto.getProductId());
        if (existingProduct == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
        List<String> oldImgsInDb = new ArrayList<>();
        if (existingProduct.getImgs() != null && !existingProduct.getImgs().isEmpty()) {
            oldImgsInDb = Arrays.asList(existingProduct.getImgs().split(";"));
        }

        // FE gửi những ảnh muốn giữ lại
        List<String> keepImgs = dto.getProductImg() != null ? dto.getProductImg().getExistingImg() : new ArrayList<>();

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

        for (ProductVariantUpdateDTO v : dto.getVariants()) {
            // Trường hợp xoá biến thể
            if (v.isDeleted()) {
                if (v.getVariantId() != null && v.getVariantId().matches("\\d+")) { // chỉ xoá trong DB nếu id số
                    productVariantRepository.deleteVariantById(Integer.parseInt(v.getVariantId()));
                }
                continue;
            }

            String finalVariantImg = null;

            // Key ảnh biến thể: variantImages_a, variantImages_b... hoặc variantImages_38
            String key = "variantImages_" + v.getVariantId();

            if (variantImageFiles.containsKey(key) && !variantImageFiles.get(key).isEmpty()) {
                // Có file mới ➜ upload, ghi đè hoặc thêm mới
                List<String> newUploaded = new ArrayList<>();
                for (MultipartFile file : variantImageFiles.get(key)) {
                    try {
                        String url = cloudinaryService.upload(file);
                        newUploaded.add(url);
                    } catch (IOException e) {
                        throw new RuntimeException("Upload ảnh thất bại!", e);
                    }
                }

                // Giữ ảnh cũ + thêm ảnh mới (nếu muốn chỉ giữ mới thì FE gửi existingImg rỗng)
                List<String> keepVariantImgs = v.getVariantImg() != null
                        ? v.getVariantImg().getExistingImg()
                        : new ArrayList<>();

                List<String> merged = new ArrayList<>(keepVariantImgs);
                merged.addAll(newUploaded);

                finalVariantImg = String.join(";", merged);

            } else {
                // Không có ảnh mới, chỉ giữ ảnh cũ
                if (v.getVariantImg() != null && v.getVariantImg().getExistingImg() != null
                        && !v.getVariantImg().getExistingImg().isEmpty()) {
                    finalVariantImg = String.join(";", v.getVariantImg().getExistingImg());
                }
            }

            v.setImg(finalVariantImg);

            // Update hoặc insert variant
            if (v.getVariantId() != null && v.getVariantId().matches("\\d+")) {
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

    @Override
    public List<NewProductDTO> getNewProduct(int limit){
        return productRepository.getNewProduct(limit);
    }

    @Override
    public ProductResponeDTO getProductById(int productId) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
        ProductResponeDTO dto = new ProductResponeDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setMinPrice(product.getMinPrice());
        dto.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            dto.setCategory(product.getCategory().getName());
            dto.setCategoryId(product.getCategory().getCategoryId());
        } else {
            dto.setCategory(null);
        }


        // Ảnh sản phẩm
        ProductImgDTO productImgDTO = new ProductImgDTO();
        if (product.getImgs() != null && !product.getImgs().isEmpty()) {
            List<String> imgs = Arrays.asList(product.getImgs().split(";"));
            productImgDTO.setExistingImg(imgs);
        } else {
            productImgDTO.setExistingImg(new ArrayList<>());
        }
        dto.setProductImg(productImgDTO);
        List<ProductVariantResponeDTO> variantList = productVariantRepository.findByProductId(product.getProductId())
                .stream()
                .map(variant -> {
                    ProductVariantResponeDTO vDTO = new ProductVariantResponeDTO();
                    vDTO.setVariantId(variant.getVariantId());
                    vDTO.setColor(variant.getColor());
                    vDTO.setPrice(variant.getPrice().doubleValue());
                    vDTO.setStockQuantity(variant.getStockQuantity());
                    vDTO.setSku(variant.getSku());
                    vDTO.setDeleted(false); // vì đang GET active

                    VariantImgDTO variantImgDTO = new VariantImgDTO();
                    if (variant.getImg() != null && !variant.getImg().isEmpty()) {
                        List<String> imgs = Arrays.asList(variant.getImg().split(";"));
                        variantImgDTO.setExistingImg(imgs);
                    } else {
                        variantImgDTO.setExistingImg(new ArrayList<>());
                    }
                    vDTO.setVariantImg(variantImgDTO);

                    return vDTO;
                }).collect(Collectors.toList());

        dto.setVariants(variantList);

        return dto;
    }

    @Override
    public List<FilterProductResponse> filterProduct(String price, int page, int size){
        return productRepository.filterProduct(price, page, size);
    }

    @Override
    public int countFilterProduct(String filterQuery){
        return productRepository.countFilterProduct(filterQuery);
    }
}
