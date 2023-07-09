package com.example.store.mapper;


import com.example.store.dto.request.ProductRequestDTO;
import com.example.store.dto.response.ProductResponseDTO;
//import com.example.store.dto.response.ProductStatisticResponseDTO;
import com.example.store.dto.response.ProductStatisticResponseDTO;
import com.example.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // id không được set null trong Mapper
//    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "inventory", source = "dto.inventory")
    @Mapping(target = "price", source = "dto.price")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "thumbnail", expression = "java(null)")

    @Mapping(target = "category.id", source = "dto.category")
    @Mapping(target = "groupProduct.id", source = "dto.groupProduct")
    @Mapping(target = "brand.id", source = "dto.brand")
    Product productRequestDTOtoProduct(ProductRequestDTO dto);

    @Mapping(target = "id", source = "p.id")
    @Mapping(target = "name", source = "p.name")
    @Mapping(target = "inventory", source = "p.inventory")
    @Mapping(target = "price", source = "p.price")
    @Mapping(target = "rate", source = "p.rate")
    @Mapping(target = "description", source = "p.description")
    @Mapping(target = "deleted", source = "p.deleted")
    @Mapping(target = "thumbnail", source = "p.thumbnail")

    @Mapping(target = "discountPrice", source = "p.discountPrice")
    @Mapping(target = "discountPercent", source = "p.discountPercent")
    @Mapping(target = "forSale", source = "p.forSale")

    @Mapping(target = "groupProductId", source = "p.groupProduct.id")
    @Mapping(target = "categoryId", source = "p.category.id")
    @Mapping(target = "brandId", source = "p.brand.id")
    @Mapping(target = "groupProduct", source = "p.groupProduct.name")
    @Mapping(target = "category", source = "p.category.name")
    @Mapping(target = "brand", source = "p.brand.name")
    ProductResponseDTO productToProductResponseDTO(Product p);


//    @Mapping(target = "id", source = "ip.product.id")
//    @Mapping(target = "name", source = "ip.product.name")
//    @Mapping(target = "quantity", source = "ip.product.inventory")
//    @Mapping(target = "price", source = "ip.product.price")
//    @Mapping(target = "quantitySales", source = "ip.quantity")
//    @Mapping(target = "totalPrice", expression = "java(null)")
//    ProductStatisticResponseDTO productToProductStatisticResponseDTO(IProductQuantity ip);
}

