package com.neobis.mobiMarket.mapper;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto entityToDto(Product product);
    Product dtoToEntity(ProductDto productDto);
    List<ProductDto> entitiesToDtos(List<Product> products);
    List<Product> dtosToEntities(List<ProductDto> productDtos);

}
