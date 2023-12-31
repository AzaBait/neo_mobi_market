package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findByName(String name);
    List<Product> getAllByUserId(Long id);
 //   ResponseEntity<Product> save(Product product);
    Optional<Product> getById(Long id);
    ResponseEntity<Product> update (Long id, ProductDto product);
    ResponseEntity<String> deleteProduct(Long id);
    List<Product> getAllProducts();
    void likeProduct(Long productId, User currentUser);

    ResponseEntity<Product> save(Product product, List<MultipartFile> imageFiles);
}
