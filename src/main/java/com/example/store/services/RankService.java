package com.example.store.services;

import com.example.store.dto.request.RankRequestDTO;
import com.example.store.dto.response.RankResponseDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;


public interface RankService {

    ResponseEntity<?> getAllRank();

    ResponseEntity<ResponseObject> createRank(RankRequestDTO rankRequestDTO);

    ResponseEntity<ResponseObject> updateRank(RankRequestDTO rankRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteRank(Long id);

    RankResponseDTO getRankById(Long id);
}

