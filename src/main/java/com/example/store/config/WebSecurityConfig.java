package com.example.store.config;

import com.example.store.dto.response.ResponseObject;
import com.example.store.filter.JwtTokenFilter;
import com.example.store.oauth.CustomOAuth2User;
import com.example.store.oauth.CustomOAuth2UserService;
import com.example.store.repositories.UserRepository;
import com.example.store.services.UserService;
import com.example.store.utils.MapHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.webjars.NotFoundException;


import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    @Autowired private JwtTokenFilter jwtTokenFilter;

    @Autowired private UserRepository userRepository;

    @Autowired private CustomOAuth2UserService oauthUserService;
//    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired AuthenticationSuccessHandler successHandler;

   @Autowired UserService userService;

    @Bean
    UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username.contains("@"))
                    return userRepository.findUserByEmail(username)
                            .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
                else
                    return userRepository.findUserByPhone(username)
                            .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
            }
        };
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .requestMatchers("/", "/home", "/auth/**", "/docs/**","/oauth2/**","/oauth/**", "/swagger-ui/index.html#/","/verify",
                        "/registration/**","/login/**", "/dashboard").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
//                    .userInfoEndpoint()
//                    .userService(oauthUserService)
//                    .and()
//                    .userInfoEndpoint().userService(oauthUserService)
//                    .and()
                    .successHandler(successHandler)
                    .and().logout().logoutSuccessUrl("/").permitAll();
        //                .and()

//                .formLogin().loginPage("/auth/login")
//                .successHandler(successHandler)
//                .and().csrf().disable()
//                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login")


        //Handle exception config
        http.exceptionHandling()
                .accessDeniedHandler(
                        ((request, response, accessDeniedException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_OK);
                            Map<String, Object> map = new HashMap<>();
                            ResponseObject responseObject = new ResponseObject(HttpStatus.UNAUTHORIZED, accessDeniedException.getMessage());
                            map = MapHelper.convertObject(responseObject);
                            response.getWriter().write(new JSONObject(map).toString());
                        })
                );

        http.exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_OK);
                            Map<String, Object> map = new HashMap<>();
                            ResponseObject responseObject = new ResponseObject(HttpStatus.UNAUTHORIZED, authException.getMessage());
                            map = MapHelper.convertObject(responseObject);
                            response.getWriter().write(new JSONObject(map).toString());
                        })
                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

