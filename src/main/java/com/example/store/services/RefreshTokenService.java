package com.example.store.services;

import com.example.store.entities.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Authentication authentication);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
}
