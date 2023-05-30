package com.example.store.mapper;

import com.example.store.dto.request.UserRequestDTO;
import com.example.store.dto.response.UserResponseDTO;
import com.example.store.dto.response.UserShipperResponseDTO;
import com.example.store.entities.User;
import com.example.store.models.IListShipper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "gender", source = "dto.gender")
    @Mapping(target = "birthday", source = "dto.birthday")
    @Mapping(target = "phone", source = "dto.phone")
    @Mapping(target = "status", source = "dto.status")
    @Mapping(target = "role.id", source = "dto.role")
//    @Mapping(target = "rank.id", source = "dto.rank")
    @Mapping(target = "password", source = "dto.password")
    @Mapping(target = "image", expression = "java(null)")
    User userRequestDTOtoUser(UserRequestDTO dto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "status", source = "user.status")
    @Mapping(target = "image", source = "user.image")
    @Mapping(target = "role", source = "user.role")

    UserResponseDTO userToUserResponseDTO(User user);

    @Mapping(target = "id", source = "l.id")
    @Mapping(target = "name", source = "l.name")
    UserShipperResponseDTO iListShipperToShipperResponseDTO(IListShipper l);
}
