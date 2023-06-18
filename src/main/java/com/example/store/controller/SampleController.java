package com.example.store.controller;


import com.example.store.dto.request.UserLoginRequestDTO;
import com.example.store.entities.User;
import com.example.store.repositories.UserRepository;
import com.example.store.services.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class SampleController {
    private static String authorizationRequestBaseUri
            = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepo;

    @GetMapping("/")
    public String home(){
        return "home";
    }
//
//    @GetMapping("/dashboard")
//    public String displayDashboard(Model model){
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
//            DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
//            model.addAttribute("userDetails", user.getAttribute("name")!= null ?user.getAttribute("name"):user.getAttribute("login"));
//        }else {
//            User user = (User) securityContext.getAuthentication().getPrincipal();
//            Optional<User> users = userRepo.findUserByEmail(user.getUsername());
//            model.addAttribute("userDetails", users.get().getName());
//        }
//        return "dashboard";
//    }

}
