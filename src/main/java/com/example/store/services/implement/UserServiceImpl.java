package com.example.store.services.implement;

import com.example.store.dto.request.UserRequestDTO;
import com.example.store.dto.request.UserUpdateRequestDTO;
import com.example.store.dto.response.AuthResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.UserResponseDTO;
import com.example.store.dto.response.UserShipperResponseDTO;
import com.example.store.entities.*;
import com.example.store.exceptions.InvalidValueException;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.UserMapper;
import com.example.store.repositories.*;
import com.example.store.services.ImageStorageService;
import com.example.store.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.UUID;



//import static com.sun.jndi.ldap.LdapClient.encodePassword;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RoleRepository roleRepository;
    @Autowired private RankRepository rankRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ImageStorageService imageStorageService;
    @Autowired private CartProductRepository cartDetailRepository;
    @Autowired private ReviewRepository feedbackRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private AddressDetailRepository addressDetailRepository;
    @Autowired private FirebaseImageServiceImpl imageService;
    @Autowired private JavaMailSender mailSender;
//    @Autowired private ImageFeedbackRepository imageFeedbackRepository;

    @Override
    public AuthResponseDTO findUserByEmailAndPassword(String email, String password) {return null;}

    @Override
    public ResponseEntity<ResponseObject> saveUser(UserRequestDTO userRequestDTO) throws MessagingException, UnsupportedEncodingException {
        User user = mapper.userRequestDTOtoUser(userRequestDTO);
        //Check phone user existed
        if(userRequestDTO.getPhone()!=null){
            Optional<User> userCheckPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
            if (userCheckPhone.isPresent()) {
                if(userCheckPhone.get().getPhone() != null){
                    throw new ResourceAlreadyExistsException("Phone user existed");
                }
            }
        }
        //Check email user existed
        Optional<User> userCheckEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
        if (userCheckEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("Email user existed");
        }
        encodePassword(user);
        //Check role already exists
        Role role = roleRepository.findRoleById(user.getRole().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + user.getRole().getId()));
        // tạo rank mặc định là thành viên hiện đang có name = "Bronze"
        Rank rank = rankRepository.findRankByName("Bronze")
                .orElseThrow(() -> new ResourceNotFoundException("Could not find bronze rank" ));
        user.setRank(rank);
        user.setPoint(0);
        user.setRole(role);

        String randomCodeVerify = RandomStringUtils.random(14,true,true);
        user.setVerificationCode(randomCodeVerify);
        // code after
        UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(userRepository.save(user));
        //send email
        sendVerificationEmail(user);
        //Create cart by user
        Cart cart = new Cart();
        cart.setUser(user);
        this.cartRepository.save(cart);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create user successfully!", userResponseDTO));
    }
    @Override
    public ResponseEntity<ResponseObject> updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = mapper.userUpdateRequestDTOtoUser(userUpdateRequestDTO);
        user.setId(id);
        User userExists = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
        //Check email user existed
        if (!user.getEmail().equals(userExists.getEmail())) {
            Optional<User> userCheckEmail = userRepository.findUserByEmail(userUpdateRequestDTO.getEmail());
            if (userCheckEmail.isPresent()){
                throw new ResourceAlreadyExistsException("Email user existed");
            }
        }
        //Check phone user existed
        if (!user.getPhone().equals(userExists.getPhone())) {
            Optional<User> userCheckPhone = userRepository.findUserByPhone(userUpdateRequestDTO.getPhone());
            if (userCheckPhone.isPresent()){
                throw new ResourceAlreadyExistsException("Phone user existed");
            }
        }
        //User Image
        if(userUpdateRequestDTO.getImage() != null){
            try {
                String fileName = imageService.save(userUpdateRequestDTO.getImage());
                user.setImage(fileName);
            } catch (Exception e) {}
        } else {
            user.setImage(userExists.getImage());
        }

        user.setStatus(true);
        user.setCreateDate(userExists.getCreateDate());
        user.setPoint(userExists.getPoint());
        user.setPassword(userExists.getPassword());
        user.setRank(userExists.getRank());
        user.setRole(userExists.getRole());
        user.setUpdateDate(new Date());

        UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(userRepository.save(user));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update User successfully!", userResponseDTO));
    }

    @Override
    public ResponseEntity<?> getAllUser(Pageable pageable) {
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        Page<User> getUserList = userRepository.findAll(pageable);
        List<User> userList = getUserList.getContent();
        for (User user : userList) {
            UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(user);
            userResponseDTO.setImage(imageService.getImageUrl(user.getImage()));
            userResponseDTOList.add(userResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOList);    }

    @Override
    public ResponseEntity<ResponseObject> softDeleteUser(Long id, boolean deleted) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
        user.setDeleted(deleted);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete user success!!!", deleted));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));

        //delete image
        if(user.getImage() != null){
            try {
                imageService.delete(user.getImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // delete cart
        Optional<Cart> getCart = cartRepository.findCartByUser(user);
        if (getCart.isPresent()) {
            List<CartProduct> cartDetailList = cartDetailRepository.findCartProductByCart(getCart.get());
            // xoa chi tiet Gio hang
            cartDetailRepository.deleteAll(cartDetailList);
            // xoa gio hang
            cartRepository.delete(getCart.get());
        }
        // delete feedback
        List<Review> feedbackList = feedbackRepository.findReviewByUser(user);
        for (Review f : feedbackList) {
            feedbackRepository.delete(f);
        }
        // delete address
        List<AddressDetail> addressDetailList = addressDetailRepository.findAddressDetailsByUser(user);
        addressDetailRepository.deleteAll(addressDetailList);

        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete user success!!!", null));
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
        updateUserRank(user);
        UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(user);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @Override
    public ResponseEntity<?> getALlShipper() {
        List<User> shipperList = userRepository.getAllShipper();
        List<UserShipperResponseDTO> userShipperResponseDTOList = new ArrayList<>();

        for (User i: shipperList) {
            UserShipperResponseDTO userShipperResponseDTO = mapper.iListShipperToShipperResponseDTO(i);
            userShipperResponseDTOList.add(userShipperResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userShipperResponseDTOList);
    }

    @Override
    public ResponseEntity<Integer> getNumberOfCustomer() {
        int numberOfCustomer = userRepository.getNumberOfCustomer()
                .orElseThrow(() -> new ResourceNotFoundException("Does not have any customer"));
        return ResponseEntity.status(HttpStatus.OK).body(numberOfCustomer);
    }

    public ResponseEntity<ResponseObject> verifyUser(String verifyCode) {
        User getUser = userRepository.findUserByVerificationCode(verifyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Verify code is incorrect"));
        getUser.setStatus(true);
        User user =  userRepository.save(getUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Verify account success!!!"));
    }

    public ResponseEntity<ResponseObject> updatePassword(Long id,String newPassword, String confirmPassword){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
        if(newPassword.equals(confirmPassword)){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject(HttpStatus.OK, "Update password successfully!!!", null));
        }
        throw new InvalidValueException("Xác nhận mật khẩu mới không khớp");
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private void updateUserRank(User user){
        //Update user point
        List<Order> orderList = orderRepository.findOrdersByUser(user);
        BigDecimal totalPaid = new BigDecimal(0);
        for(Order order: orderList){
            if(order.getStatus().equals("Done")){
                totalPaid = totalPaid.add(order.getTotalPrice()).setScale(0, RoundingMode.UP);
            }
        }
        double userPoint = user.getPoint();
        userPoint = totalPaid.doubleValue()/100000;
        user.setPoint(userPoint);

        //UpdateRank
        Rank silver = rankRepository.findRankByName("Silver")
                .orElseThrow(() -> new ResourceNotFoundException("Could not find silver rank "));
        Rank gold = rankRepository.findRankByName("Gold")
                .orElseThrow(() -> new ResourceNotFoundException("Could not find gold rank "));
        Rank diamond = rankRepository.findRankByName("Diamond")
                .orElseThrow(() -> new ResourceNotFoundException("Could not find diamond rank "));
        if(userPoint >= 200){
            user.setRank(diamond);
        }
        if(userPoint>=100 && userPoint<200){
            user.setRank(gold);
        }
        if(userPoint>=50 && userPoint<100){
            user.setRank(silver);
        }
    }

    private void sendVerificationEmail(User user)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "GEAR STORE";
        String verifyUrl = "http://localhost:8080/verify?code=" + user.getVerificationCode();
        String mailContent = "<p>Dear " + user.getName() + ",<p><br>"
                + "Please click the link below to verify your registration to GEAR Store:<br>"
                + "<h3><a href = \"" + verifyUrl + "\">VERIFY</a></h3>"
                + "Thank you,<br>" + "From GEAR Store with love.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("longluong290901@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendEmailForUser(String email, int typeMail) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with this email !!!"));

        String senderName = "GEAR STORE";
        String verifyUrl ="http://localhost:8080/verify?code=" + user.getVerificationCode();
        String mailContent = "<p>Dear " + user.getName() + ",<p><br>"
                + "Please click the link below to verify your registration to GEAR Store:<br>"
                + "<h3><a href = \"" + verifyUrl + "\">CLICK TO VERIFY</a></h3>"
                + "Thank you,<br>" + "From GEAR Store with love.";

        String defaultPassword = RandomStringUtils.random(14,true,true);

        String mail4Password = "<p>Dear " + user.getName() + ",<p><br>"
                + "Please click the link below to confirm reset your password in GEAR Store account:<br>"
                + "<h3>Your new password is: </h3>" + defaultPassword + "<br>"
                + "<h3><a href = \"http://localhost:3000/login\">CLICK TO BACK TO LOGIN PAGE</a></h3>"
                + "Thank you,<br>" + "From GEAR Store with love.";

        user.setPassword(defaultPassword);
        encodePassword(user);
        userRepository.save(user);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        if(typeMail == 0) // 0 for resend Verification mail to active account
        {
            messageHelper.setFrom("longluong290901@gmail.com", senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Please verify your registration");
            messageHelper.setText(mailContent, true);
            mailSender.send(message);
        }
        if(typeMail == 1) // 1 for forgot password
        {
            messageHelper.setFrom("longluong290901@gmail.com", senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Please verify for reset your password");
            messageHelper.setText(mail4Password, true);
            mailSender.send(message);
        }
    }

    public boolean updateUserStatus (Long id, boolean status){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
        user.setStatus(status);
        this.userRepository.save(user);
        return status;
    }

}
