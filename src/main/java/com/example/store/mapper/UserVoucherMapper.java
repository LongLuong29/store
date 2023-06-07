package com.example.store.mapper;

import com.example.store.dto.response.UserVoucherResponseDTO;
import com.example.store.entities.UserVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserVoucherMapper {
  @Mapping(target = "id", source = "p.user.id")
  @Mapping(target = "name", source = "p.user.name")
  @Mapping(target = "voucher", source = "p.voucher")
  UserVoucherResponseDTO userVoucherToUserVoucherResponseDTO(UserVoucher p);
}
