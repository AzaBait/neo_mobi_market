package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.mapper.ProductMapper;
import com.neobis.mobiMarket.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.MemberSubstitution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    @PostMapping("/save")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        Product product = productService.save(productMapper.dtoToEntity(productDto)).getBody();
        ProductDto savedProductDto = productMapper.entityToDto(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDto);
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<List<ProductDto>> getProductsByUserId(@PathVariable Long userId) {
       try {
           List<Product> products = productService.getAllByUserId(userId);

        if (!products.isEmpty()) {
            List<ProductDto> productDtos = ProductMapper.INSTANCE.entitiesToDtos(products);
            return ResponseEntity.ok(productDtos);
        }else {
            return ResponseEntity.notFound().build();
        }
       }catch (EntityNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
       }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product updatedProduct = productService.update(id, productDto).getBody();
        if (updatedProduct != null) {
            ProductDto productDto1 = productMapper.entityToDto(updatedProduct);
            return ResponseEntity.ok(productDto1);
        }else {
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
