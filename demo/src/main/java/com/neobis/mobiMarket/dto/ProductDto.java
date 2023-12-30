package com.neobis.mobiMarket.dto;

import com.neobis.mobiMarket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDto {

    private long id;
    private String name;
    private String shortDescription;
    private String detailedDescription;
    private double price;
    private List<String> images;
    
}
