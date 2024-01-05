package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.mapper.ProductMapper;
import com.neobis.mobiMarket.service.CloudinaryService;
import com.neobis.mobiMarket.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.MemberSubstitution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private CloudinaryService cloudinaryService;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/save")
    public ResponseEntity<ProductDto> saveProduct(@ModelAttribute ProductDto productDto,
                                                  @RequestParam("file") List<MultipartFile> file) {
        try {
            Product product = productMapper.dtoToEntity(productDto);
            ResponseEntity<Product> savedProductResponse = productService.save(product, file);
            Product savedProduct = savedProductResponse.getBody();
            ProductDto savedProductDto = productMapper.entityToDto(savedProduct);
            return ResponseEntity.status(savedProductResponse.getStatusCode()).body(savedProductDto);
        } catch (Exception e) {
            logger.error("Error while saving product with images", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @DeleteMapping("/{productId}/images/{publicId}")
    public ResponseEntity<Product> deleteProductImage(@PathVariable Long productId, @PathVariable String publicId) {
        try {
            List<String> publicIds = productService.getProductImagePublicIds(productId);

            if (publicIds.contains(publicId)) {
                ResponseEntity<Product> response = productService.deleteProductImage(productId, publicId);
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error deleting product image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{productId}/image-public-ids")
    public ResponseEntity<List<String>> getProductImagePublicIds(@PathVariable Long productId) {
        try {
            List<String> imagePublicIds = productService.getProductImagePublicIds(productId);
            return ResponseEntity.ok(imagePublicIds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<ProductDto>> getProductsByUserId(@PathVariable Long userId) {
        try {
            List<Product> products = productService.getAllByUserId(userId);

            if (!products.isEmpty()) {
                List<ProductDto> productDtos = ProductMapper.INSTANCE.entitiesToDtos(products);
                return ResponseEntity.ok(productDtos);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product updatedProduct = productService.update(id, productDto).getBody();
        if (updatedProduct != null) {
            ProductDto productDto1 = productMapper.entityToDto(updatedProduct);
            return ResponseEntity.ok(productDto1);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return productService.getById(id).map(product -> ResponseEntity.ok(productMapper.entityToDto(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> getAll() {
        List<ProductDto> productDtos = productMapper.entitiesToDtos(productService.getAllProducts());
        return ResponseEntity.ok().body(productDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/{productId}/like")
    public ResponseEntity<String> likeProduct(@PathVariable Long productId, @MemberSubstitution.Current User currentuser) {
        productService.likeProduct(productId, currentuser);
        return ResponseEntity.ok("Product liked successfully!");
    }


}
