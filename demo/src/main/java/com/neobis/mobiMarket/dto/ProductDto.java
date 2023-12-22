package com.neobis.mobiMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {

    private long id;
    private String name;
    private String shortDescription;
    private String detailedDescription;
    private double price;
    private String image;
}
