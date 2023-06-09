package com.example.store.services.implement;

import com.example.store.dto.request.ProductRequestDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired private FirebaseImageServiceImpl imageService;
    @Autowired private ImageReviewRepository imageReviewRepository;


    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

    @Override
    public ResponseEntity<?> getAllReviewOnTrading(Pageable pageable) {
        Page<Review> getReviewList = reviewRepository.findAll(pageable);
        List<Review> reviewList = getReviewList.getContent();
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        for (Review f : reviewList) {
            ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(f);
            if(f.getProduct().getThumbnail()!=null){
                reviewResponseDTO.setProductThumbnail(imageService.getImageUrl(f.getProduct().getThumbnail()));
            }
            reviewResponseDTOList.add(reviewResponseDTO);

        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ItemTotalPage(reviewResponseDTOList, getReviewList.getTotalPages()));
    }

    @Override
    public ResponseEntity<?> getAllReviewByProduct(Pageable pageable, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        Page<Review> getReviewList = reviewRepository.findReviewByProduct(pageable,product);
        List<Review> reviewList = getReviewList.getContent();
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(review);
            if(product.getThumbnail()!=null){
                reviewResponseDTO.setProductThumbnail(imageService.getImageUrl(product.getThumbnail()));
            }
            List<ImageReview> imageReviewList = imageReviewRepository.findImageReviewByReview(review);
            String[] images = new String[imageReviewList.size()];
            if(imageReviewList != null){
                for (int i=0; i < imageReviewList.size(); i++){
                    images[i] = imageService.getImageUrl(imageReviewList.get(i).getPath());
                }
            }

            reviewResponseDTO.setImages(images);
            reviewResponseDTO.setUserImage(imageService.getImageUrl(review.getUser().getImage()));
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

        checkExits(user, product);
        checkBought(user, product);
        review.setUser(user);
        review.setProduct(product);

        Review reviewSaved = reviewRepository.save(review);
        ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(reviewSaved);

        if(reviewRequestDTO.getImages() != null){
            try {
                reviewResponseDTO.setImages(saveImage(reviewRequestDTO, reviewSaved));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Thêm mới Review thành công!", reviewResponseDTO));
    }

    private String[] saveImage(ReviewRequestDTO reviewRequestDTO, Review review) throws IOException {
        int numberOfFile = reviewRequestDTO.getImages().length;
        int i =0;
        String[] images = new String[numberOfFile];
        String[] imageResponse = new String[numberOfFile];
        //delete exist images
        List<ImageReview> imageReviewList = imageReviewRepository.findImageReviewByReview(review);
        this.imageReviewRepository.deleteAll(imageReviewList);
        for(ImageReview imageReview: imageReviewList){
            this.imageService.delete(imageReview.getPath());
        }

        for(MultipartFile file: reviewRequestDTO.getImages()){
            String fileName = imageService.save(file);
            images[i] = fileName;
            imageResponse[i] = imageService.getImageUrl(fileName);
            i++;
        }

        for (String path : images){
            ImageReview imageReview = new ImageReview();
            imageReview.setReview(review);
            imageReview.setPath(path);
            this.imageReviewRepository.save(imageReview);
        }
        return images;
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
        List<ImageReview> imageReviewList = imageReviewRepository.findImageReviewByReview(review);

        String[] images = new String[imageReviewList.size()];

        if(imageReviewList!=null){
            for (int i=0; i < imageReviewList.size(); i++){
                images[i] = imageService.getImageUrl(imageReviewList.get(i).getPath());
            }
        }

        ReviewResponseDTO reviewResponseDTO = mapper.reviewToReviewResponseDTO(review);
        reviewResponseDTO.setImages(images);
        reviewResponseDTO.setUserImage(imageService.getImageUrl(review.getUser().getImage()));
        return reviewResponseDTO;
    }

    private User checkBought(User user, Product product){
        List<Order> orderList = orderRepository.findOrdersByUser(user);
        for(Order order: orderList){
            Optional<OrderProduct> orderProduct = orderProductRepository.findOrderProductByOrderAndProduct(order,product);
            if( orderProduct.isPresent() && orderProduct.get().getProduct() == product)
            {
                if(orderProduct.get().getOrder().getStatus().equals("Done")){
                    return user;
                }
            }
        }
        throw new ResourceNotFoundException("Người dùng chưa mua hoặc chưa hoàn thành việc mua sản phẩm  này");
    }

    private void checkExits(User user, Product product){
        Optional<Review> getReview = reviewRepository.findReviewByUserAndProduct(user, product);
        if(getReview.isPresent()){
            throw new ResourceNotFoundException("Người dùng đã đánh giá sản phẩm này rồi");
        }
    }
}
