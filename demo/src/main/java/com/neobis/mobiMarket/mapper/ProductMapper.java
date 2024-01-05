package com.neobis.mobiMarket.mapper;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Image;
import com.neobis.mobiMarket.entity.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
//
//    ProductDto entityToDto(Product product);
//    Product dtoToEntity(ProductDto productDto);
//    List<ProductDto> entitiesToDtos(List<Product> products);
//    List<Product> dtosToEntities(List<ProductDto> productDtos)

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "images", target = "images", qualifiedByName = "mapImagesToStrings")
    ProductDto entityToDto(Product product);

    @Named("mapImagesToStrings")
    default List<String> mapImagesToStrings(List<Image> images) {
        if (images == null) {
            return null;
        }

        return images.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }

    @InheritInverseConfiguration
    @Mapping(source = "images", target = "images", qualifiedByName = "mapStringsToImages")
    Product dtoToEntity(ProductDto productDto);

    @Named("mapStringsToImages")
    default List<Image> mapStringsToImages(List<String> imageUrls) {
        if (imageUrls == null) {
            return null;
        }

        return imageUrls.stream()
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    return image;
                })
                .collect(Collectors.toList());
    }

    List<ProductDto> entitiesToDtos(List<Product> products);

    List<Product> dtosToEntities(List<ProductDto> productDtos);
}



