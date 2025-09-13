package com.project.keyboard.controller;

import com.cloudinary.Api;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.keyboard.dto.PriceOption;
import com.project.keyboard.dto.request.ProductRequestDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.request.ProductVariantRequestDTO;
import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.dto.response.FilterProductResponse;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.page.PagedResponse;
import com.project.keyboard.dto.response.product.NewProductDTO;
import com.project.keyboard.dto.response.product.ProductResponeDTO;
import com.project.keyboard.dto.response.revenue.TopSellingProductDTO;
import com.project.keyboard.entity.Product;
import com.project.keyboard.system.ProductService;
import com.project.keyboard.system.cloudinary.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final List<PriceOption> priceOptionList = List.of(
            new PriceOption("lt-100000", " And min_price <= 100000"),
            new PriceOption("100000-200000", " And min_price > 100000 And min_price <= 200000"),
            new PriceOption("200000-300000", " And min_price > 200000 And min_price <= 300000"),
            new PriceOption("300000-500000", " And min_price > 300000 And min_price <= 500000"),
            new PriceOption("500000-1000000", " And min_price > 500000 And min_price <= 1000000"),
            new PriceOption("gt-1000000", " And min_price > 1000000")
    );

    @GetMapping("/getListProduct")
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponeDTO>>> getListProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try {
            List<ProductResponeDTO> products = productService.getListProduct(page, size);
            int totalElements = productService.countProducts();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PagedResponse<ProductResponeDTO> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(products);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);

            return ResponseEntity.ok(
                    new ApiResponse<>("Lay san pham thanh cong", 200, "success", pagedResponse, null)
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
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            ObjectMapper mapper = new ObjectMapper();

            // Parse product
            ProductRequestDTO productDTO = mapper.readValue(productJson, ProductRequestDTO.class);

            // Parse variants nếu có
            List<ProductVariantRequestDTO> variantsDTO = new ArrayList<>();
            if (variantsJson != null && !variantsJson.trim().isEmpty() && !variantsJson.equals("[]")) {
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
    public ResponseEntity<ApiResponse<Boolean>> disableProduct(@PathVariable int productId,
                                                               HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
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
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            ObjectMapper mapper = new ObjectMapper();
            ProductUpdateDTO dto = mapper.readValue(productJson, ProductUpdateDTO.class);

            // Lấy các file biến thể riêng
            Map<String, List<MultipartFile>> variantImageFiles = new HashMap<>();
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                String fileName = fileNames.next(); // ví dụ: variantImages_a, variantImages_38
                if (fileName.startsWith("variantImages_")) {
                    List<MultipartFile> files = request.getFiles(fileName);
                    if (files != null && !files.isEmpty()) {
                        variantImageFiles.put(fileName, files);
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

    @GetMapping("/getNewProduct")
    public ResponseEntity<ApiResponse<List<NewProductDTO>>> getNewProduct(@RequestParam(defaultValue = "5") int limit){
        try {
            List<NewProductDTO> list = productService.getNewProduct(limit);
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách sản phẩm mới thành công", 200, "success", list, null)
            );
        }catch (Exception e){
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách sản phẩm mới thất bại", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PagedResponse<FilterProductResponse>>> filterProduct(@RequestParam(defaultValue = "") String price,
                                                                                           @RequestParam(defaultValue = "") String stock,
                                                                                           @RequestParam(defaultValue = "0") int page,
                                                                                           @RequestParam(defaultValue = "10") int size
    ){
        try{
            String defaulPriceQuery = "";
            for (PriceOption option : priceOptionList){
                if (price.trim().equals(option.getPriceOption())){
                    defaulPriceQuery = option.getQuerySql();
                    break;
                }
            }
            if (stock.trim().equals("availabe")){
                defaulPriceQuery = defaulPriceQuery + " and stock_quantity > 0";
            }
            List<FilterProductResponse> products = productService.filterProduct(defaulPriceQuery, page, size);
            int totalElements = productService.countFilterProduct(defaulPriceQuery);
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PagedResponse<FilterProductResponse> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(products);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);

            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách sản phẩm thành công", 200, "success", pagedResponse, null));
        }catch (Exception e){
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách sản phẩm thất bại", 500, "error", null, e.getMessage()));
        }
    }
}
