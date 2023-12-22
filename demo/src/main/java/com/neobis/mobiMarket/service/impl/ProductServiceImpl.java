package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.repository.ProductRepo;
import com.neobis.mobiMarket.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    @Override
    public Optional<Product> findByName(String name) {
        Product product = productRepo.findByName(name).orElseThrow(()
                -> new IllegalStateException("Product with name " + name + " does not exist!"));
        return Optional.ofNullable((product));
    }

    @Override
    public List<Product> getAllByUserId(Long id) {
        List<Product> products = productRepo.getAllByUserId(id);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Products not found for user with id: " + id);
        }
        return products;
    }

    @Override
    public ResponseEntity<Product> save(Product product) {
        Product product1;
        try {
            product1 = productRepo.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Could not save product!");
        }
        return ResponseEntity.ok(product1);
    }

    @Override
    public Optional<Product> getById(Long id) {
        Product product = productRepo.findById(id).orElseThrow(()
                -> new IllegalStateException("Product with id " + id + " does not exist!"));
        return Optional.ofNullable(product);
    }

    @Override
    public ResponseEntity<Product> update(Long id, ProductDto product) {

        Product productInDB;
        try {
            productInDB = productRepo.findById(id).orElseThrow(() ->
                    new IllegalStateException("Product with id " + id + " does not exist!"));
            productInDB.setName(product.getName());
            productInDB.setShortDescription(product.getShortDescription());
            productInDB.setDetailedDescription(product.getDetailedDescription());
            productInDB.setPrice(product.getPrice());
            productInDB.setImage(product.getImage());


            productRepo.save(productInDB);
            System.out.println("Product successfully updated!");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(productInDB);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() ->
                new IllegalStateException("Product with id " + id + " does not exist!"));
        productRepo.deleteById(product.getId());
        return ResponseEntity.ok("Product with id " + id +" successfully deleted!");
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
}
