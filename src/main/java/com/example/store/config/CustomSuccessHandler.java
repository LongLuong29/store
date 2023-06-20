package com.example.store.config;

import java.io.IOException;
import java.util.Optional;

import com.example.store.dto.request.UserRequestDTO;
import com.example.store.entities.User;
import com.example.store.mapper.UserMapper;
import com.example.store.oauth.CustomOAuth2User;
import com.example.store.repositories.RoleRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.UserService;
import com.example.store.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired UserRepository userRepo;
    @Autowired RoleRepository roleRepository;
    @Autowired JwtTokenUtil jwtUtil;
    @Autowired UserService userService;
    @Autowired UserMapper userMapper;
    @Autowired AuthenticationManager auth;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,

                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;
        UsernamePasswordAuthenticationToken authenticationToken = null;
        String username = null;
        User user4Token = new User();
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            username = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";
            Optional<User> getUser = userRepo.findUserByEmail(username);
            if (!getUser.isPresent()) {
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
            authenticationToken = new UsernamePasswordAuthenticationToken(username, "default");
            authentication = auth.authenticate(authenticationToken);
            user4Token = (User) authentication.getPrincipal();
        }
        String accessToken = jwtUtil.generateToken(user4Token);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), accessToken);
//        redirectUrl = "/";
//        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl + name);

//*****************************************************************************************************************

        //        System.out.println("AuthenticationSuccessHandler invoked");
//        System.out.println("Authentication name: " + authentication.getName());
//        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//        String email = oauthUser.getEmail();
//        Optional<User> getUser = userRepo.findUserByEmail(email);
//            if(!getUser.isPresent()) {
//                UserRequestDTO user = new UserRequestDTO();
////                user.setName(userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login"));
//                user.setEmail(email);
//                user.setName(oauthUser.getName());
//                user.setPassword(("default"));
//                user.setRole(1L);
//                try {
//                    userService.saveUser(user);
//                } catch (MessagingException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        userService.processOAuthPostLogin(oauthUser.getEmail());
//        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
//        String email = oauthUser.getAttribute("email");
//        userService.processOAuthPostLogin(email);
//        response.sendRedirect("/");
    }
}

