package com.example.projet_pai.filter;

import com.example.projet_pai.service.Impl.UserDetailsServiceImpl;
import com.example.projet_pai.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    void doFilterInternal_ShouldAuthenticateUser_WhenTokenIsValid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(jwtUtil.extractUsername("valid_token")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.validateToken("valid_token", "testuser")).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(jwtUtil, times(1)).validateToken("valid_token", "testuser");
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotAuthenticateUser_WhenTokenIsInvalid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtUtil.extractUsername("invalid_token")).thenReturn("testuser");
        when(jwtUtil.validateToken("invalid_token", "testuser")).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(chain, times(1)).doFilter(request, response);
    }
}
