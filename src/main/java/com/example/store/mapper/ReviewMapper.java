package com.example.store.mapper;


import com.example.store.dto.request.ReviewRequestDTO;
import com.example.store.dto.response.ReviewResponseDTO;
import com.example.store.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "vote", source = "dto.vote")
    @Mapping(target = "content", source = "dto.content")
    @Mapping(target = "user", expression = "java(null)")
    @Mapping(target = "product", expression = "java(null)")
    @Mapping(target = "createDate", expression = "java(null)")
    @Mapping(target = "updateDate", expression = "java(null)")
    Review reviewRequestDTOToReview(ReviewRequestDTO dto);


    @Mapping(target = "id", source = "f.id")
    @Mapping(target = "content", source = "f.content")
    @Mapping(target = "vote", source = "f.vote")
    @Mapping(target = "user", source = "f.user.id")
    @Mapping(target = "userName", source = "f.user.name")
    @Mapping(target = "product", source = "f.product.id")
    @Mapping(target = "productName", source = "f.product.name")
    @Mapping(target = "productThumbnail", source = "f.product.thumbnail")
    @Mapping(target = "createDate", source = "f.createDate")
    @Mapping(target = "updateDate", source = "f.updateDate")
    ReviewResponseDTO reviewToReviewResponseDTO(Review f);
}
