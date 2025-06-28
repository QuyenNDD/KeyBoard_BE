package com.project.keyboard.controller;

import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.category.ProductCategoryDTO;
import com.project.keyboard.entity.ProductCategory;
import com.project.keyboard.system.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
