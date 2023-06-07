package com.example.store.mapper;

import com.example.store.dto.request.RankRequestDTO;
import com.example.store.dto.response.RankResponseDTO;
import com.example.store.entities.Rank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RankMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "discount", source = "dto.discount")
//    @Mapping(target = "point", source = "dto.point")
//    @Mapping(target = "icon", source = "dto.icon")
    @Mapping(target = "color", source = "dto.color")
    @Mapping(target = "description", source = "dto.description")
    Rank rankRequestDTOToRank(RankRequestDTO dto);

    @Mapping(target = "id",source ="rank.id" )
    @Mapping(target = "name", source = "rank.name")
    @Mapping(target = "discount", source ="rank.discount" )
//    @Mapping(target = "point", source ="rank.point" )
//    @Mapping(target = "icon", source = "rank.icon")
    @Mapping(target = "color", source ="rank.color" )
    @Mapping(target = "description", source = "rank.description")
    RankResponseDTO rankToRankResponseDTO(Rank rank);
}
