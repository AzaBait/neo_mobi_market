package com.neobis.mobiMarket.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.neobis.mobiMarket.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public CloudinaryServiceImpl() {
    }

    public String uploadImage(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadImage)
                .toList();
    }

    public void deleteProductImage(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Deletion result: " + result);

        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary", e);
        }
    }

    public String extractPublicId(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Invalid imageUrl");
        }
        System.out.println("Original URL: " + imageUrl);
        int lastSlashIndex = imageUrl.lastIndexOf('/');

        if (lastSlashIndex != -1) {
            String filenameWithExtension = imageUrl.substring(lastSlashIndex + 1);
            int lastDotIndex = filenameWithExtension.lastIndexOf('.');
            String publicId = (lastDotIndex != -1) ? filenameWithExtension.substring(0, lastDotIndex) : filenameWithExtension;

            System.out.println("Public ID: " + publicId);
            return publicId;
        } else {
            throw new IllegalArgumentException("Invalid imageUrl format");
        }
    }
}
