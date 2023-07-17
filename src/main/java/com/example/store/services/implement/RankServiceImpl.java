package com.example.store.services.implement;

import com.example.store.dto.request.RankRequestDTO;
import com.example.store.dto.response.RankResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Rank;
import com.example.store.entities.User;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.RankMapper;
import com.example.store.repositories.RankRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.RankService;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RankServiceImpl implements RankService {

    private final RankMapper mapper = Mappers.getMapper(RankMapper.class);

    @Autowired private RankRepository rankRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getAllRank() {
        List<Rank> getRankList = rankRepository.findAll();
        List<RankResponseDTO> rankResponseDTOList = new ArrayList<>();
        for(Rank b: getRankList){
            RankResponseDTO rankResponseDTO = mapper.rankToRankResponseDTO(b);
            rankResponseDTOList.add(rankResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rankResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> createRank(RankRequestDTO rankRequestDTO) {
        Rank rank = mapper.rankRequestDTOToRank(rankRequestDTO);
        rank = checkExits(rank);

        Rank rankSaved = rankRepository.save(rank);
        RankResponseDTO rankResponseDTO = mapper.rankToRankResponseDTO(rankSaved);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create rank successfully!", rankResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateRank(RankRequestDTO rankRequestDTO, Long id) {
        Rank rank = mapper.rankRequestDTOToRank(rankRequestDTO);
        Rank getRank = rankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find rank with ID = " + id));
        rank.setId(id);
        rank.setCreatedDate(getRank.getCreatedDate());
        rank = checkExits(rank);

        Rank rankSaved = rankRepository.save(rank);
        RankResponseDTO rankResponseDTO = mapper.rankToRankResponseDTO(rankSaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update rank successfully!", rankResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteRank(Long id) {
        Rank getRank = rankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find rank with ID = " + id));
        rankRepository.delete(getRank);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Delete rank successfully!"));
    }

    @Override
    public RankResponseDTO getRankById(Long id) {
        Rank getRank = rankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find rank with ID = " + id));
        RankResponseDTO rankResponseDTO = mapper.rankToRankResponseDTO(getRank);
        return rankResponseDTO;
    }

    @Override
    public RankResponseDTO getRankByUserId(Long userId) {
        User getUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        Long getRankId = getUser.getRank().getId();
        Rank getRank = rankRepository.findRankById(getRankId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find rank with this User "));
        RankResponseDTO rankResponseDTO = mapper.rankToRankResponseDTO(getRank);
        return rankResponseDTO;
    }

    @Override
    public ResponseEntity<?> resetAllUserRank(){
        List<User> userList = userRepository.findAll();
        Rank bronze = rankRepository.findRankByName("Bronze")
                .orElseThrow(() ->new ResourceNotFoundException("Could not find rank with Bronze rank "));
        for (User u: userList){
            if(u.getRole().getId() == 1){
                u.setRank(bronze);
                u.setPoint(0);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Reset rank successfully");
    }

    //check brand name exits or not
    private Rank checkExits(Rank rank){
        Optional<Rank> getRank = rankRepository.findRankByName(rank.getName());
        if(getRank.isPresent()){
            if(rank.getId() == null){
                throw new ResourceAlreadyExistsException("Rank name already exists");
            } else {
                if (rank.getId() != getRank.get().getId()) {
                    throw new ResourceAlreadyExistsException("Rank name already exists");
                }
            }
        }
        return rank;
    }
}
