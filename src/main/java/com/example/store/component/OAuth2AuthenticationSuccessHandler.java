package com.example.store.component;

import java.io.IOException;
import java.util.Optional;

import com.example.store.dto.request.UserRequestDTO;
import com.example.store.entities.RefreshToken;
import com.example.store.entities.User;
import com.example.store.entities.principal.UserPrincipal;
import com.example.store.exceptions.BadRequestException;
import com.example.store.mapper.UserMapper;
import com.example.store.oauth.CustomOAuth2User;
import com.example.store.repositories.RoleRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.RefreshTokenService;
import com.example.store.services.UserService;
import com.example.store.utils.CookieUtils;
import com.example.store.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.Cookie;
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    @Autowired UserRepository userRepo;
    @Autowired RoleRepository roleRepository;
    @Autowired JwtTokenUtil jwtUtil;
    @Autowired UserService userService;
    @Autowired UserMapper userMapper;
    @Autowired AuthenticationManager auth;
    @Autowired ServletContext servletContext;
    @Autowired RefreshTokenService refreshTokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,

                                        Authentication authentication) throws IOException, ServletException {
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
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String token = jwtUtil.generateToken(user4Token);
        String targetUrl = redirectUri.orElse("http://localhost:3000/login");
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
//                .queryParam("refreshToken", refreshToken.getToken())
//                .queryParam("userId", userPrincipal.getId())
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

//        request.setAttribute("accessToken", accessToken);
//        RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/oauth2/getToken");
//        dispatcher.forward(request, response);

//        response.setContentType("application/json");
//        new ObjectMapper().writeValue(response.getOutputStream(), accessToken);
//        redirectUrl = "/oauth2/getToken";
//        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}

