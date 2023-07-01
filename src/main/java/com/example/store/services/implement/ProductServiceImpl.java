package com.example.store.services.implement;

import com.example.store.dto.request.ProductRequestDTO;
import com.example.store.dto.response.ProductGalleryDTO;
import com.example.store.dto.response.ProductResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.*;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repositories.*;
import com.example.store.repositories.ProductRepository;
import com.example.store.services.ImageStorageService;
import com.example.store.services.ProductService;
import com.example.store.utils.Utils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired private ProductRepository productRepository;
    @Autowired private DiscountRepository discountRepository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private GroupProductRepository groupProductRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ImageStorageService imageStorageService;
    @Autowired private ImageProductRepository imageProductRepository;
    @Autowired private ProductAttributeRepository attributeProductRepository;
    @Autowired private ProductDiscountRepository productDiscountRepository;
    @Autowired private FirebaseImageServiceImpl imageService;

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);
    @Override
    public ResponseEntity<?> getAllProductOnTrading(Pageable pageable) {
        Page<Product> getProductList = productRepository.findAll(pageable);
        List<Product> productList = getProductList.getContent();
        List<ProductGalleryDTO> productGalleryDTOList = new ArrayList<>();
        for (Product product : productList){
            int discount = 0;
            Optional<Integer> getDiscount = discountRepository.findPercentByProductId(product.getId()/*, new Date()*/);
            if (getDiscount.isPresent()){
                discount = getDiscount.get();
            }
            ProductGalleryDTO productGalleryDTO = toProductGalleryDTO(product, discount);
            productGalleryDTOList.add(productGalleryDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productGalleryDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> createProduct(ProductRequestDTO productRequestDTO) {
        Product product = mapper.productRequestDTOtoProduct(productRequestDTO);
        product = checkExists(product);
        //Set thumbnail
        if (productRequestDTO.getThumbnail() != null){
            try {
                String fileName = imageService.save(productRequestDTO.getThumbnail());
                product.setThumbnail(fileName);
            } catch (Exception e) {}
        }
        //set deleted = false
        product.setDeleted(false);

        Product productSaved = productRepository.save(product);
        ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(productSaved);
        if (productRequestDTO.getImages() != null){
            try {
                productResponseDTO.setImages(saveImage(productRequestDTO, productSaved));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create product successfully!", productResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateProduct(ProductRequestDTO productRequestDTO, Long id) {
        Product product = mapper.productRequestDTOtoProduct(productRequestDTO);
        Product getProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + id));
        product.setId(id);
        product = checkExists(product);

        //set exist
        product.setDeleted(false);
        product.setRate(getProduct.getRate());
        product.setCreatedDate(getProduct.getCreatedDate());
//        product.setInventory(getProduct.getInventory() + productRequestDTO.getInventory());
        //Set thumbnail
        if (productRequestDTO.getThumbnail() != null){
            try {
                String fileName = imageService.save(productRequestDTO.getThumbnail());
                product.setThumbnail(fileName);
                imageService.delete(getProduct.getThumbnail());
            } catch (Exception e) {}
        }
        else{
            product.setThumbnail(getProduct.getThumbnail());
        }

        Product productSaved = productRepository.save(product);
        ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(productSaved);
        //Save images
        if (productRequestDTO.getImages() != null){
            try {
                productResponseDTO.setImages(saveImage(productRequestDTO, productSaved));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update product successfully!", productResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteProduct(Long id) {
        Product getProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + id));
        //Delete image of product
        List<ImageProduct> imageProductList = imageProductRepository.findImageProductByProduct(getProduct);
        for (ImageProduct imageProduct : imageProductList){
            imageStorageService.deleteFile(imageProduct.getPath(), "product/thumbnail");
            imageProductRepository.delete(imageProduct);
        }
        //Delete attribute of product
        List<ProductAttribute> productAttributeList = attributeProductRepository.findProductAttributeByProduct(getProduct);
        this.attributeProductRepository.deleteAll(productAttributeList);
        //Delete discount of product
        List<ProductDiscount> productDiscountList = productDiscountRepository.findProductDiscountByProduct(getProduct);
        this.productDiscountRepository.deleteAll(productDiscountList);

        productRepository.delete(getProduct);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete product successfully!"));
    }

    @Override
    public ResponseEntity<?> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + id));
        ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(product);

        // images
        List<ImageProduct> imageProductList = imageProductRepository.findImageProductByProduct(product);
        String[] images = new String[imageProductList.size()];
        for (int i=0; i < imageProductList.size(); i++){
            images[i] = imageService.getImageUrl(imageProductList.get(i).getPath());
        }
        //product discount
        Optional<Integer> getDiscount = discountRepository.findPercentByProductId(product.getId()/*, new Date()*/);
        double discount = 0;
        if (getDiscount.isPresent()){
            discount = getDiscount.get();
            productResponseDTO.setDiscountPercent(discount);
        }
        BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf( (100- discount) / (double) 100));
        if (discount == 0){
            price = product.getPrice();
            productResponseDTO.setDiscountPercent(0);
        }
        //set exists
        productResponseDTO.setThumbnail(imageService.getImageUrl(product.getThumbnail()));
        productResponseDTO.setDiscountPrice(price);
        productResponseDTO.setImages(images);
        //calculate review
        List<Review> reviewList = reviewRepository.findReviewsByProduct(product);
        double calRate = Utils.calculateAvgRate(reviewList);
        productResponseDTO.setRate(calRate);

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<?> search(String search, int page, int size) {
        return null;
    }

    private Product checkExists(Product product){
        //Check product name exists
        Optional<Product> getProduct = productRepository.findProductByName(product.getName());
        if (getProduct.isPresent()){
            if (product.getId() == null){
                throw new ResourceAlreadyExistsException("Product name is exists");
            } else {
                if (product.getId() != getProduct.get().getId()){
                    throw new ResourceAlreadyExistsException("Product name is exists");
                }
            }
        }
        //Check brand already exists
        Brand brand = brandRepository.findById(product.getBrand().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + product.getBrand().getId()));
        product.setBrand(brand);
        //Check category already exists
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find category with ID = " + product.getCategory().getId()));
        product.setCategory(category);
        //Check groupProduct already exists
        GroupProduct groupProduct = groupProductRepository.findById(product.getGroupProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find group product with ID = " + product.getGroupProduct().getId()));
        product.setGroupProduct(groupProduct);
        return product;
    }

    private String[] saveImage(ProductRequestDTO productRequestDTO, Product product) throws IOException {
        int numberOfFile = productRequestDTO.getImages().length;
        int i =0;
        String[] images = new String[numberOfFile];

        //delete exist images
        List<ImageProduct> imageProductList = imageProductRepository.findImageProductByProduct(product);
        this.imageProductRepository.deleteAll(imageProductList);
        for(ImageProduct imageProduct: imageProductList){
            this.imageService.delete(imageProduct.getPath());
        }

        for(MultipartFile file: productRequestDTO.getImages()){
            String fileName = imageService.save(file);
            images[i] = fileName;
            i++;
        }
        for (String path : images){
            ImageProduct imageProduct = new ImageProduct();
            imageProduct.setProduct(product);
            imageProduct.setPath(path);
            this.imageProductRepository.save(imageProduct);
        }
        return images;
    }

    private ProductGalleryDTO toProductGalleryDTO(Product product, double discount){
        ProductGalleryDTO productGalleryDTO = new ProductGalleryDTO();
        productGalleryDTO.setId(product.getId());
        productGalleryDTO.setName(product.getName());
        productGalleryDTO.setPrice(product.getPrice());
        String thumbnailUrl = imageService.getImageUrl(product.getThumbnail());
        productGalleryDTO.setThumbnail(thumbnailUrl);
        productGalleryDTO.setDiscount(discount);
        productGalleryDTO.setGroupProduct(product.getGroupProduct().getName());
        productGalleryDTO.setBrand(product.getBrand().getName());
        productGalleryDTO.setCategory(product.getCategory().getName());
        BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf( (100- discount) / (double) 100));
        if (discount == 0){
            price = product.getPrice();
        }
        productGalleryDTO.setDiscountPrice(price);
        List<Review> reviewList = reviewRepository.findReviewsByProduct(product);
        productGalleryDTO.setRate(Utils.calculateAvgRate(reviewList));

        return productGalleryDTO;
    }
}
