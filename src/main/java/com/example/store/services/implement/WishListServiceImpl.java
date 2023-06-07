package com.example.store.services.implement;

import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.WishListResponseDTO;
import com.example.store.entities.Product;
import com.example.store.entities.User;
import com.example.store.entities.WishList;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.WishListMapper;
import com.example.store.repositories.ProductRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.repositories.WishListRepository;
import com.example.store.services.WishListService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishListServiceImpl implements WishListService {
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private WishListRepository wishListRepository;

    private final WishListMapper wishListMapper = Mappers.getMapper(WishListMapper.class);

    @Override
    public ResponseEntity<?> getUserWishList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Could not find user with this id = "+userId));
        List<WishList> wishListList = wishListRepository.findWishListByUser(user);
        List<WishListResponseDTO> wishListResponseDTOList = new ArrayList<>();
        for(WishList w: wishListList){
            WishListResponseDTO wishListResponseDTO = wishListMapper.wishListToWishListResponseDTO(w);
            wishListResponseDTOList.add(wishListResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(wishListResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> createUserWishList(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Could not find user with this id = "+userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));

        Optional<WishList> getWishList = wishListRepository.findWishListByUserAndProduct(user, product);
        if(getWishList.isPresent()){
            throw new ResourceAlreadyExistsException("Wishlist of product is exists");
        }
        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setProduct(product);
        WishList wishListSaved = wishListRepository.save(wishList);
        WishListResponseDTO wishListResponseDTO = wishListMapper.wishListToWishListResponseDTO(wishListSaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create wishlist successfully !!!", wishListResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteUserWishList(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Could not find user with this id = "+userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        WishList wishList = wishListRepository.findWishListByUserAndProduct(user,product)
                .orElseThrow(()-> new ResourceNotFoundException
                        ("Could not found wishlist with product id = "+productId+" and user id = "+userId));
        this.wishListRepository.delete(wishList);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete wishlist of user successfully!!!"));
    }
}
