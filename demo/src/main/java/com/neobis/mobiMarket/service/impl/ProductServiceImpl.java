package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.repository.ProductRepo;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.CloudinaryService;
import com.neobis.mobiMarket.service.ProductService;
import com.neobis.mobiMarket.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

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

//    @Override
//    public ResponseEntity<Product> save(Product product) {
//        Product product1;
//        try {
//            product1 = productRepo.save(product);
//        } catch (Exception e) {
//            throw new RuntimeException("Could not save product!");
//        }
//        return ResponseEntity.ok(product1);
//    }

//    @Override
//    public ResponseEntity<Product> save(Product product, List<MultipartFile> imageFiles) {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            com.neobis.mobiMarket.entity.User user = (com.neobis.mobiMarket.entity.User) authentication.getPrincipal();
//
//            product.setUser(user);
//            // Сохраняем продукт в репозитории
//            Product savedProduct = productRepo.save(product);
//
//            // Загружаем фотографии в облачное хранилище и обновляем продукт с URL изображений
//            List<String> imageUrls = cloudinaryService.uploadImages(imageFiles);
//            savedProduct.setImages(imageUrls);
//
//            // Возвращаем ResponseEntity с сохраненным продуктом
//            return ResponseEntity.ok(savedProduct);
//        } catch (DataAccessException e) {
//            logger.error("Error saving product to the database", e);
//            throw new RuntimeException("Could not save product to the database", e);
//        }
//    }

    @Override
    public ResponseEntity<Product> save(Product product, List<MultipartFile> imageFiles) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            logger.info("Type of principal in ProductService.save(): " + principal.getClass().getName());

            if (principal instanceof UserDetails) {

                UserDetails userDetails = (UserDetails) principal;
                Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());

                if (userOptional.isPresent()) {

                    User user = userOptional.get();
                    product.setUser(user);
                    Product savedProduct = productRepo.save(product);
                    List<String> imageUrls = cloudinaryService.uploadImages(imageFiles);
                    List<String> updatedImages = new ArrayList<>(savedProduct.getImages());
                    updatedImages.addAll(imageUrls);
                    savedProduct.setImages(updatedImages);
                    productRepo.save(savedProduct);
                    logger.debug("Updated images: {}", updatedImages);
                    return ResponseEntity.ok(savedProduct);
                } else {
                    throw new RuntimeException("User not found for username: " + userDetails.getUsername());
                }
            } else {
                logger.error("Principal is not an instance of UserDetails. Principal details: " + principal.toString());
                throw new RuntimeException("Principal is not an instance of UserDetails");
            }

        } catch (DataAccessException e) {
            logger.error("Error saving product to the database", e);
            throw new RuntimeException("Could not save product to the database", e);
        }
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
            productInDB.setImages(product.getImages());


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

    @Override
    public void likeProduct(Long productId, User currentUser) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        currentUser.getLikes().add(product);
        if (!currentUser.getFavorites().contains(product)) {
            currentUser.getFavorites().add(product);
            product.getFavoriteBy().add(currentUser);
        }
        userRepo.save(currentUser);
        productRepo.save(product);
    }
}
