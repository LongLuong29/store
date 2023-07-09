package com.example.store.controller;

import com.example.store.dto.request.UserRequestDTO;
import com.example.store.dto.request.UserUpdateRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.UserResponseDTO;
import com.example.store.services.FirebaseImageService;
import com.example.store.services.ImageStorageService;
import com.example.store.services.UserService;
import com.example.store.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
  @Autowired private UserService userService;

  @Autowired private FirebaseImageService imageService;

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createUser(@ModelAttribute @Valid UserRequestDTO userRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    return userService.saveUser(userRequestDTO/*, siteUrl*/);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> updateUser(@PathVariable(name = "id") Long id,
                                                   @ModelAttribute @Valid UserUpdateRequestDTO userUpdateRequestDTO){
    return userService.updateUser(id, userUpdateRequestDTO);
  }

  @GetMapping(value = "/image")
  public String getUserImage(@RequestParam(name = "filename") String filename) {
    return imageService.getImageUrl(filename);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllUser(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
                                          @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page){
    Pageable pageable = PageRequest.of(page - 1, size);
    return userService.getAllUser(pageable);
  }

  @GetMapping(value = "/shipper")
  public ResponseEntity<?> getAllShipper(){ return userService.getALlShipper(); }

  @GetMapping(value = "/user-amount")
  public ResponseEntity<Integer> getNumberOfCustomer(){ return userService.getNumberOfCustomer(); }

  @DeleteMapping(value = "/softDelete")
  public ResponseEntity<ResponseObject> softDeleteUser(@RequestParam(name = "id")Long id,
                                                       @RequestParam(name = "deleted") boolean deleted){
    return userService.softDeleteUser(id,deleted);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> deleteUser(@PathVariable(name = "id") Long id){
    return userService.deleteUser(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(name = "id") Long id){
    return userService.getUserById(id);
  }

  @PutMapping(value = "/{id}/password")
  public ResponseEntity<ResponseObject> changePassword(@PathVariable(name = "id") Long id,
                                                       @Valid String newPassword,
                                                       @Valid String confirmPassword){
    return userService.updatePassword(id, newPassword, confirmPassword);
  }

  @GetMapping(value = "/password/send-mail")
  public void sendMail4ChangePassword(@RequestParam(name = "email") String email) throws MessagingException, UnsupportedEncodingException {
    userService.sendEmailForUser(email,1);
  }
}
