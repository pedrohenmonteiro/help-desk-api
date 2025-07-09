package br.com.pedromonteiro.auth_service_api.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedromonteiro.auth_service_api.controller.AuthController;
import br.com.pedromonteiro.auth_service_api.security.JWTAuthenticationImpl;
import br.com.pedromonteiro.auth_service_api.service.RefreshTokenService;
import br.com.pedromonteiro.auth_service_api.util.JWTUtils;
import models.requests.AuthenticateRequest;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.responses.RefreshTokenResponse;

@RestController
public class AuthControllerImpl implements AuthController {

    private final JWTUtils jwtUtils;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;

    public AuthControllerImpl(
            final JWTUtils jwtUtils,
            final AuthenticationConfiguration authenticationConfiguration,
            final RefreshTokenService refreshTokenService
    ) {
        this.jwtUtils = jwtUtils;
        this.authenticationConfiguration = authenticationConfiguration;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                new JWTAuthenticationImpl(jwtUtils, authenticationConfiguration.getAuthenticationManager())
                        .authenticate(request)
                        .withRefreshToken(refreshTokenService.save(request.email()).getId())
        );
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok().body(
                refreshTokenService.refreshToken(request.refreshToken())
        );
    }
}
