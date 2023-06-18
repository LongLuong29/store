package com.example.store.config;

import java.io.IOException;
import java.util.Optional;

import com.example.store.dto.request.UserRequestDTO;
import com.example.store.entities.User;
import com.example.store.repositories.RoleRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;
        if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User  userDetails = (DefaultOAuth2User ) authentication.getPrincipal();
            String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
            Optional<User> getUser = userRepo.findUserByEmail(username);
            if(!getUser.isPresent()) {
                UserRequestDTO user = new UserRequestDTO();

//                user.setName(userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login"));
                user.setEmail(username);
                user.setName(userDetails.getAttribute("name"));
                user.setPassword(("default"));
                user.setRole(1L);
                try {
                     userService.saveUser(user);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        redirectUrl = "/";
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
