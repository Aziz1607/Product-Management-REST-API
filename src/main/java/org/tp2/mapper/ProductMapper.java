package org.tp2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.tp2.dto.ProductDTO;
import org.tp2.model.Product;

@Mapper(componentModel = "spring") // ✅ this is critical
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);
}
