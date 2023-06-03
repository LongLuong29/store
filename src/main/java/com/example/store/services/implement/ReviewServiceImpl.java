package com.example.store.services.implement;

import com.example.store.dto.request.ReviewRequestDTO;
import com.example.store.dto.response.ReviewResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.*;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.ReviewMapper;
import com.example.store.models.ItemTotalPage;
import com.example.store.repositories.*;
import com.example.store.services.ReviewService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;


    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

    @Override
    public ResponseEntity<?> getAllReviewOnTrading(Pageable pageable) {
        Page<Review> getReviewList = reviewRepository.findAll(pageable);
        List<Review> reviewList = getReviewList.getContent();
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        for (Review f : reviewList) {
            ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(f);
            reviewResponseDTOList.add(reviewResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ItemTotalPage(reviewResponseDTOList, getReviewList.getTotalPages()));
    }

    @Override
    public ResponseEntity<?> getAllReviewByProduct(Pageable pageable, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        Page<Review> getReviewList = reviewRepository.findReviewByProduct(pageable,product);
        List<Review> reviewList = getReviewList.getContent();
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        for (Review f : reviewList) {
            ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(f);
            reviewResponseDTOList.add(reviewResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ItemTotalPage(reviewResponseDTOList,getReviewList.getTotalPages()));
    }

    @Override
    public ResponseEntity<ResponseObject> createReview(ReviewRequestDTO reviewRequestDTO) {
        Review review = mapper.reviewRequestDTOToReview(reviewRequestDTO);

        User user = userRepository.findById(reviewRequestDTO.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User với ID = " + reviewRequestDTO.getUser()));
        Product product = productRepository.findById(reviewRequestDTO.getProduct()).
                orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Product với ID = " + reviewRequestDTO.getProduct()));
        checkBought(user, product);
        review.setUser(user);
        review.setProduct(product);

        Review reviewSaved = reviewRepository.save(review);
        ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(reviewSaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Thêm mới Review thành công!", reviewResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateReview(ReviewRequestDTO reviewRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> deleteReview(Long reviewId) {
        Review getReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find review with ID = " + reviewId));

        reviewRepository.delete(getReview);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK, "Xóa Review thành công!")
        );
    }

    @Override
    public ReviewResponseDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Review với ID = " + reviewId));
        ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(review);

        return reviewResponseDTO;
    }

    private User checkBought(User user, Product product){
        User getUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User với ID = " + user.getId()));
        Product getProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Product với ID = " + product.getId()));

        List<Order> orderList = orderRepository.findOrdersByUser(getUser);
        for(Order order: orderList){
            List<OrderProduct> orderProductList = orderProductRepository.findOrderProductByOrder(order);
            for(OrderProduct op: orderProductList){
                if(op.getProduct() != getProduct)
                {
                    return user;
                }
            }
        }
        throw new ResourceNotFoundException("Người dùng chưa mua sản phẩm này");
    }
}
