package com.neobis.mobiMarket.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudinaryService {

    String uploadImage(MultipartFile file);

    List<String> uploadImages(List<MultipartFile> files);
}
