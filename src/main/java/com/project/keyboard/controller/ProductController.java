package com.project.keyboard.controller;

import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.entity.Product;
import com.project.keyboard.system.ProductService;
import com.project.keyboard.system.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/getListProduct")
    public ResponseEntity<ApiResponse<List<Product>>> getListProduct(){
        try {
            List<Product> users = productService.getListProduct();
            return ResponseEntity.ok(
                    new ApiResponse<>("Lay san pham thanh cong", 200, "success", users, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PostMapping("/addProduct")
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestPart("product") ProductRequestDTO productDTO,
                                                           @RequestPart("images") MultipartFile[] images){
        try {
            if (productDTO.getVariants().size() != images.length) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>("Số lượng ảnh không khớp với số biến thể", 400, "error", null, null));
            }

            // Upload từng ảnh lên Cloudinary và gán URL vào biến thể
            for (int i = 0; i < images.length; i++) {
                String url = cloudinaryService.upload(images[i]);
                productDTO.getVariants().get(i).setImg(url);
            }

            productService.createProduct(productDTO);
            return ResponseEntity.ok(
                    new ApiResponse<>("Them san pham thanh cong", 200, "success", null, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
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

    @PutMapping("/updateProduct")
    public ResponseEntity<ApiResponse<Void>> updateProduct(@RequestPart("product") ProductUpdateDTO dto,
                                                           @RequestPart(value = "images", required = false) MultipartFile[] images){
        try {
            productService.updateProductWithImages(dto, images);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật sản phẩm thành công", 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Lỗi khi cập nhật", 500, "error", null, e.getMessage()));
        }
    }
}
