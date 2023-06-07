package com.example.store.mapper;

import com.example.store.dto.request.VoucherRequestDTO;
import com.example.store.dto.response.UserVoucherResponseDTO;
import com.example.store.dto.response.VoucherResponseDTO;
import com.example.store.entities.UserVoucher;
import com.example.store.entities.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

  //  @Mapping(target = "id", source = "p.id")
  @Mapping(target = "title", source = "dto.title")
  @Mapping(target = "percent", source = "dto.percent")
  @Mapping(target = "description", source = "dto.description")
  @Mapping(target = "thumbnail", source = "dto.thumbnail")
  @Mapping(target = "type", source = "dto.type")
  @Mapping(target = "code", source = "dto.code")
  @Mapping(target = "startDate", source = "dto.startDate")
  @Mapping(target = "endDate", source = "dto.endDate")
  Voucher voucherRequestDTOtoVoucher(VoucherRequestDTO dto);
  @Mapping(target = "id", source = "p.id")
  @Mapping(target = "title", source = "p.title")
  @Mapping(target = "percent", source = "p.percent")
  @Mapping(target = "status", source = "p.status")
  @Mapping(target = "description", source = "p.description")
  @Mapping(target = "thumbnail", source = "p.thumbnail")
  @Mapping(target = "type", source = "p.type")
  @Mapping(target = "code", source = "p.code")
  @Mapping(target = "startDate", source = "p.startDate")
  @Mapping(target = "endDate", source = "p.endDate")
  VoucherResponseDTO voucherToVoucherResponseDTO(Voucher p);
}
