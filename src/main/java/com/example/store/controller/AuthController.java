package com.example.store.controller;

import com.example.store.dto.request.AuthRequestDTO;
import com.example.store.dto.response.AuthResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.User;
import com.example.store.services.UserService;
import com.example.store.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class  AuthController {
  @Autowired UserService userService;

  @Autowired AuthenticationManager auth;

  @Autowired JwtTokenUtil jwtUtil;

  @PostMapping (value = "/auth/login")
  public ResponseEntity<ResponseObject> getUserByEmailAndPassword(@RequestBody @Valid AuthRequestDTO request) {
    UsernamePasswordAuthenticationToken authenticationToken = null;
    if (request.getEmail() == null){
      authenticationToken = new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword());
    } else {
      authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    }
    Authentication authentication = auth.authenticate(authenticationToken);
    User user = (User) authentication.getPrincipal();
    String accessToken = jwtUtil.generateToken(user);
    AuthResponseDTO authResponseDTO = new AuthResponseDTO(user.getName(), accessToken);
    return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, "Login Successfully", authResponseDTO));
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<ResponseObject> verifyUser(@RequestParam(name = "code") String verifyCode){
    return userService.verifyUser(verifyCode);
  }
}
