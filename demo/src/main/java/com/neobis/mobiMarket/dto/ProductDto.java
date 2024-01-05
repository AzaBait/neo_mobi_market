package com.neobis.mobiMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDto {

    //   private Long id;
    private String name;
    private String shortDescription;
    private String detailedDescription;
    private Double price;
    private List<String> images;

}
