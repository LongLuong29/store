package com.example.store.controller;

import com.example.store.dto.request.RankRequestDTO;
import com.example.store.dto.response.RankResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.RankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/rank")
public class RankController {

    @Autowired private RankService rankService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllRank() {
        return rankService.getAllRank();
    }

    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> createRank(@ModelAttribute @Valid RankRequestDTO rankRequestDTO){
        return rankService.createRank(rankRequestDTO);
    }

    @PutMapping(value = "")
    public ResponseEntity<ResponseObject> updateRank(@ModelAttribute @Valid RankRequestDTO rankRequestDTO, @RequestParam(name = "id") Long id){
        return rankService.updateRank(rankRequestDTO, id);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<ResponseObject> deleteRank(@RequestParam(name = "id") Long id){
        return rankService.deleteRank(id);
    }

    @GetMapping(value = "/{id}")
    public RankResponseDTO getRankById(@PathVariable(name = "id") Long id){
        return rankService.getRankById(id);
    }

}
