package com.example.store.config;

import com.example.store.dto.response.ResponseObject;
import com.example.store.filter.JwtTokenFilter;
import com.example.store.repositories.UserRepository;
import com.example.store.utils.MapHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.webjars.NotFoundException;


import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    @Autowired private JwtTokenFilter jwtTokenFilter;

    @Autowired private UserRepository userRepository;

    @Autowired AuthenticationSuccessHandler successHandler;

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
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .requestMatchers("/auth/login", "/docs/**", "/swagger-ui/index.html#/","/verify",
                        "/oauth2/**","/registration/**","/login/**", "/", "/home", "/dashboard")
                .permitAll()
                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/login").successHandler(successHandler)
                .and().csrf().disable()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and().oauth2Login().successHandler(successHandler);


//        //Handle exception config
//        http.exceptionHandling()
//                .accessDeniedHandler(
//                        ((request, response, accessDeniedException) -> {
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            response.setStatus(HttpServletResponse.SC_OK);
//                            Map<String, Object> map = new HashMap<>();
//                            ResponseObject responseObject = new ResponseObject(HttpStatus.UNAUTHORIZED, accessDeniedException.getMessage());
//                            map = MapHelper.convertObject(responseObject);
//                            response.getWriter().write(new JSONObject(map).toString());
//                        })
//                );
//
//        http.exceptionHandling()
//                .authenticationEntryPoint(
//                        ((request, response, authException) -> {
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            response.setStatus(HttpServletResponse.SC_OK);
//                            Map<String, Object> map = new HashMap<>();
//                            ResponseObject responseObject = new ResponseObject(HttpStatus.UNAUTHORIZED, authException.getMessage());
//                            map = MapHelper.convertObject(responseObject);
//                            response.getWriter().write(new JSONObject(map).toString());
//                        })
//                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

