package com.project.keyboard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.request.ProductVariantRequestDTO;
import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.product.ProductResponeDTO;
import com.project.keyboard.dto.response.revenue.TopSellingProductDTO;
import com.project.keyboard.entity.Product;
import com.project.keyboard.system.ProductService;
import com.project.keyboard.system.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/getListProduct")
    public ResponseEntity<ApiResponse<List<ProductResponeDTO>>> getListProduct(){
        try {
            List<ProductResponeDTO> users = productService.getListProduct();
            return ResponseEntity.ok(
                    new ApiResponse<>("Lay san pham thanh cong", 200, "success", users, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<ApiResponse<ProductResponeDTO>> getProductById(@PathVariable int productId){
        try {
            ProductResponeDTO product = productService.getProductById(productId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Lay san pham thanh cong", 200, "success", product, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> addProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "variants", required = false) String variantsJson,
            @RequestParam(value = "productImages", required = false) List<MultipartFile> productImages,
            MultipartHttpServletRequest request
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Parse product
            ProductRequestDTO productDTO = mapper.readValue(productJson, ProductRequestDTO.class);

            // Parse variants nếu có
            List<ProductVariantRequestDTO> variantsDTO = new ArrayList<>();
            if (variantsJson != null && !variantsJson.trim().isEmpty()) {
                variantsDTO = mapper.readValue(variantsJson, new TypeReference<List<ProductVariantRequestDTO>>() {});
            }

            // Upload ảnh sản phẩm chính
            List<String> productImageUrls = new ArrayList<>();
            if (productImages != null && !productImages.isEmpty()) {
                for (MultipartFile img : productImages) {
                    String url = cloudinaryService.upload(img);
                    productImageUrls.add(url);
                }
            }

            // Gán URL ảnh sản phẩm
            productDTO.setImgs(String.join(";", productImageUrls));

            // Map ảnh biến thể nếu có
            for (int i = 0; i < variantsDTO.size(); i++) {
                String paramName = "variantImages_" + i;
                List<MultipartFile> files = request.getFiles(paramName);
                if (!files.isEmpty()) {
                    String url = cloudinaryService.upload(files.get(0)); // 1 biến thể 1 ảnh
                    variantsDTO.get(i).setImg(url);
                }
            }

            productDTO.setVariants(variantsDTO);

            productService.createProduct(productDTO);

            return ResponseEntity.ok(new ApiResponse<>("Thêm sản phẩm thành công", 200, "success", null, null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }



    @PutMapping("/disableProduct/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> disableProduct(@PathVariable int productId){
        try {
            boolean ok = productService.disableProduct(productId);
            if (ok){
                return ResponseEntity.ok(
                        new ApiResponse<>("Khóa sản phẩm thành công", 200, "success", null, null));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                        new ApiResponse<>("Khóa sản phẩm thất bại", 500, "error", null, null));
            }
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>("Không tìm thấy sản phẩm", 404, "error", null, e.getMessage())
            );
        }
    }

    @PutMapping(value = "/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "productImages", required = false) List<MultipartFile> productImages,
            MultipartHttpServletRequest request
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductUpdateDTO dto = mapper.readValue(productJson, ProductUpdateDTO.class);

            // Lấy các file biến thể riêng
            Map<Integer, MultipartFile> variantImageFiles = new HashMap<>();
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                String fileName = fileNames.next();
                if (fileName.startsWith("variantImages_")) {
                    String idxStr = fileName.substring("variantImages_".length());
                    int index = Integer.parseInt(idxStr);
                    MultipartFile file = request.getFile(fileName);
                    if (file != null && !file.isEmpty()) {
                        variantImageFiles.put(index, file);
                    }
                }
            }
            productService.updateProductWithImages(dto, productImages, variantImageFiles);

            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi cập nhật", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/topSellingProduct")
    public ResponseEntity<ApiResponse<List<TopSellingProductDTO>>> getTopSellingProduct(@RequestParam(defaultValue = "3") int limit){
        try {
            List<TopSellingProductDTO> list = productService.getTopSellingProduct(limit);
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách sản phẩm bán chạy thành công", 200, "success", list, null)
            );
        }catch (Exception e){
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách sản phẩm bán chạy thất bại", 500, "error", null, e.getMessage())
            );
        }
    }
}
