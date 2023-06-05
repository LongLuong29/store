package com.example.store.services;

import com.example.store.dto.request.UserPasswordRequestDTO;
import com.example.store.dto.request.UserRequestDTO;
import com.example.store.dto.request.UserUpdateRequestDTO;
import com.example.store.dto.response.AuthResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.UserResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {
    AuthResponseDTO findUserByEmailAndPassword(String email, String password);
    ResponseEntity<ResponseObject> saveUser(UserRequestDTO userRequestDTO/*, String siteUrl*/)
            throws MessagingException, UnsupportedEncodingException;
    ResponseEntity<ResponseObject> updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDTO);
    ResponseEntity<?> getAllUser(Pageable pageable);
    ResponseEntity<ResponseObject> deleteUser(Long id);
    ResponseEntity<UserResponseDTO> getUserById(Long id);
    ResponseEntity<ResponseObject> verifyUser(String verifyCode);
    ResponseEntity<ResponseObject> updatePassword(Long id,String newPassword, String confirmPassword);
    ResponseEntity<?> getALlShipper();
    ResponseEntity<Integer> getNumberOfCustomer();
    //    boolean checkPassword(Long id, String currentPassword);


}
