package com.neobis.mobiMarket.mapper;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-07T15:11:10+0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto entityToDto(Product product) {
        if ( product == null ) {
            return null;
        }

        List<String> images = null;
        String name = null;
        String shortDescription = null;
        String detailedDescription = null;
        Double price = null;

        images = mapImagesToStrings( product.getImages() );
        name = product.getName();
        shortDescription = product.getShortDescription();
        detailedDescription = product.getDetailedDescription();
        price = product.getPrice();

        ProductDto productDto = new ProductDto( name, shortDescription, detailedDescription, price, images );

        return productDto;
    }

    @Override
    public Product dtoToEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setImages( mapStringsToImages( productDto.getImages() ) );
        product.setName( productDto.getName() );
        product.setShortDescription( productDto.getShortDescription() );
        product.setDetailedDescription( productDto.getDetailedDescription() );
        if ( productDto.getPrice() != null ) {
            product.setPrice( productDto.getPrice() );
        }

        return product;
    }

    @Override
    public List<ProductDto> entitiesToDtos(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( products.size() );
        for ( Product product : products ) {
            list.add( entityToDto( product ) );
        }

        return list;
    }

    @Override
    public List<Product> dtosToEntities(List<ProductDto> productDtos) {
        if ( productDtos == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( productDtos.size() );
        for ( ProductDto productDto : productDtos ) {
            list.add( dtoToEntity( productDto ) );
        }

        return list;
    }
}
