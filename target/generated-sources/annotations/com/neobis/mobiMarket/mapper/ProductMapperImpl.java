package com.neobis.mobiMarket.mapper;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-21T20:23:25+0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto entityToDto(Product product) {
        if ( product == null ) {
            return null;
        }

        long id = 0L;
        String name = null;
        String shortDescription = null;
        String detailedDescription = null;
        double price = 0.0d;
        String image = null;

        id = product.getId();
        name = product.getName();
        shortDescription = product.getShortDescription();
        detailedDescription = product.getDetailedDescription();
        price = product.getPrice();
        image = product.getImage();

        ProductDto productDto = new ProductDto( id, name, shortDescription, detailedDescription, price, image );

        return productDto;
    }

    @Override
    public Product dtoToEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productDto.getId() );
        product.setName( productDto.getName() );
        product.setShortDescription( productDto.getShortDescription() );
        product.setDetailedDescription( productDto.getDetailedDescription() );
        product.setPrice( productDto.getPrice() );
        product.setImage( productDto.getImage() );

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
