package com.project.keyboard.controller;

import com.project.keyboard.dto.request.ProductCategoryRequestDTO;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.category.DetailCatgoryDTO;
import com.project.keyboard.dto.response.category.ProductCategoryDTO;
import com.project.keyboard.entity.ProductCategory;
import com.project.keyboard.system.ProductCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/getListProductCategory")
    public ResponseEntity<ApiResponse<List<ProductCategoryDTO>>> getListProductCategory(){
        try{
            List<ProductCategoryDTO> productCategoryDTOS = productCategoryService.getListProductCategory();
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách danh mục sản phẩm thành công", 200, "success", productCategoryDTOS, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PostMapping("/createProductCategory")
    public ResponseEntity<ApiResponse<Void>> createProductCategory(@RequestBody ProductCategoryRequestDTO dto,
                                                                   HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            ProductCategory newProductCategory = productCategoryService.createCategory(dto);
            return ResponseEntity.ok(new ApiResponse<>("Tạo danh mục sản phẩm thành công", 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PutMapping("/updateProductCategory/{id}")
    public ResponseEntity<ApiResponse<Void>> updateProductCategory(@RequestBody ProductCategoryRequestDTO dto,
                                                                   @PathVariable int id,
                                                                   HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            ProductCategory productCategory = productCategoryService.updateCategory(dto, id);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật danh mục sản phẩm thành công", 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @DeleteMapping("/deleteProductCategory/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductCategory(@PathVariable int id,
                                                                   HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            productCategoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa danh mục sản phẩm thành công", 200, "success", null, null));
        }catch (Exception e){
            return ResponseEntity.ok(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/detailCategory/{id}")
    public ResponseEntity<ApiResponse<DetailCatgoryDTO>> detailCategory(@PathVariable int id, HttpServletRequest request){
        try{
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }

            DetailCatgoryDTO dto = productCategoryService.getDetailCategory(id);
            return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết danh mục sản phẩm thành công", 200, "success", dto, null));
        }catch (Exception e){
            return ResponseEntity.ok(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }
}
