package com.example.store.controller;


import com.example.store.dto.request.UserLoginRequestDTO;
import com.example.store.dto.response.AuthResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.User;
import com.example.store.repositories.UserRepository;
import com.example.store.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class SampleController {

    @Autowired
    UserRepository userRepo;

    @GetMapping("/oauth2/getToken")
    public ResponseEntity<ResponseObject> googleToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String accessToken = request.getAttribute("accessToken").toString();
//        String username = "";
//        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, accessToken);
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, "Login Successfully", accessToken));
//            response.setContentType("application/json");
//            new ObjectMapper().writeValue(response.getOutputStream(), accessToken);

    }
}
