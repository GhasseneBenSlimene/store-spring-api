package com.ghassenebenslimene.store.auth;

import com.ghassenebenslimene.store.users.Role;
import com.ghassenebenslimene.store.users.User;
import com.ghassenebenslimene.store.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void loginReturnsTokens() {
        var request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("secret");

        var user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setName("User");
        user.setRole(Role.USER);

        var accessToken = mock(Jwt.class);
        var refreshToken = mock(Jwt.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("user@example.com", "secret"));
        when(userRepository.findByEmail(eq("user@example.com"))).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(eq(user))).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(eq(user))).thenReturn(refreshToken);

        var response = authService.login(request);

        assertThat(response.getAccessToken()).isSameAs(accessToken);
        assertThat(response.getRefreshToken()).isSameAs(refreshToken);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void refreshAccessTokenRejectsInvalidToken() {
        when(jwtService.parseToken(eq("bad-token"))).thenReturn(null);

        assertThatThrownBy(() -> authService.refreshAccessToken("bad-token"))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refreshAccessTokenRejectsExpiredToken() {
        var expiredJwt = mock(Jwt.class);
        when(expiredJwt.isExpired()).thenReturn(true);
        when(jwtService.parseToken(eq("expired-token"))).thenReturn(expiredJwt);

        assertThatThrownBy(() -> authService.refreshAccessToken("expired-token"))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refreshAccessTokenReturnsNewToken() {
        var jwt = mock(Jwt.class);
        when(jwt.isExpired()).thenReturn(false);
        when(jwt.getUserId()).thenReturn(42L);

        var user = new User();
        user.setId(42L);
        user.setEmail("user@example.com");
        user.setRole(Role.USER);

        var accessToken = mock(Jwt.class);

        when(jwtService.parseToken(eq("refresh-token"))).thenReturn(jwt);
        when(userRepository.findById(eq(42L))).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(eq(user))).thenReturn(accessToken);

        var result = authService.refreshAccessToken("refresh-token");

        assertThat(result).isSameAs(accessToken);
    }

    @Test
    void getCurrentUserReturnsUserFromContext() {
        var user = new User();
        user.setId(10L);
        user.setEmail("user@example.com");
        user.setRole(Role.USER);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(10L, null)
        );

        when(userRepository.findById(eq(10L))).thenReturn(Optional.of(user));

        var result = authService.getCurrentUser();

        assertThat(result).isSameAs(user);
    }
}
