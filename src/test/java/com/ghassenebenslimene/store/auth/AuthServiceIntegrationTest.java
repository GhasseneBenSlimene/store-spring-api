package com.ghassenebenslimene.store.auth;

import com.ghassenebenslimene.store.users.Role;
import com.ghassenebenslimene.store.users.User;
import com.ghassenebenslimene.store.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceIntegrationTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void loginAndRefreshTokensWork() {
        var user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.setRole(Role.USER);
        userRepository.save(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("test@example.com", "secret"));

        var request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("secret");

        var response = authService.login(request);

        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();

        var parsedAccess = jwtService.parseToken(response.getAccessToken().toString());
        assertThat(parsedAccess).isNotNull();
        assertThat(parsedAccess.getUserId()).isEqualTo(user.getId());

        var newAccess = authService.refreshAccessToken(response.getRefreshToken().toString());
        assertThat(newAccess).isNotNull();
        assertThat(newAccess.getUserId()).isEqualTo(user.getId());
    }
}
